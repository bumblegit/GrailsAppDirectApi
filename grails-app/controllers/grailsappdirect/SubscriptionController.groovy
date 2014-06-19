package grailsappdirect

import grails.converters.XML
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.apache.http.HttpStatus

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class SubscriptionController extends RestfulController{

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    SubscriptionController() {
        super(Subscription)
    }

    def index(Integer max) {
        println "WS Index!!"
        params.max = Math.min(max ?: 10, 100)
        respond Subscription.list(params), model: [subscriptionInstanceCount: Subscription.count()]
    }

    //Take in mind code 200
    def show(Subscription subscriptionInstance) {
        respond subscriptionInstance
    }

    /*
    Test for action:
        curl -i -H "Accept: application/xml"  -H "Content-Type: application/xml" -X POST -d "" http://localhost:8080/GrailsAppDirect/api/subscriptions

    Error codes for order subscriptions:
        -UNAUTHORIZED: This error code is returned when users try any action that is not authorized for that particular application. For example, if an application does not allow the original creator to be unassigned.
        -(*)CONFIGURATION_ERROR: This error code is returned when the vendor endpoint is not currently configured.
        -INVALID_RESPONSE: This error code is returned when the vendor was unable to process the event fetched from AppDirect.
        -UNKNOWN_ERROR: This error code may be used when none of the other error codes apply.

        * Not implemented yet
    * */

    @Transactional
    def save() {
        println "WS Create!!!"

        /*Build for coming subscription. Once twe have de response for the call to appdirect data, we need to parse the XML and fetch it into Subscription's objects*/
        Subscription subscription = new Subscription()
        Marketplace marketplace = buildMarketplace()
        Company company = buildCompany()
        Creator creator = buildCreator()
        Account account = buildAccount()
        def users = buildUsers(subscription)
        subscription = buildSubscription(marketplace, account, company, creator, users, subscription)

        //Authorization and fetch the data

        /*Result creation*/
        Result result = new Result()

        if (subscription == null || subscription.hasErrors()) {
            result.success = false

            result.errorCode = ErrorCode.UNKNOWN_ERROR
            result.message = "Subscription NOT created!"
        }

        println "create - subscription: "
        println " -account: "+subscription.marketplace.partner
        println " -creator: "+subscription.creator.firstName+" "+subscription.creator.lastName
        println " -account: "+subscription.account.identifier
        println " -company: "+subscription.company.name
        println " -users: "
        subscription.users.each {
            println "   -user: $it.openId"
        }

        if (subscription.save(flush: true)) {
            result.success = true
            result.errorCode = null
            //We are going to use the company's UUID as accountIdentifier. We will have it available for future events
            result.accountIdentifier = subscription.company.uuid
            result.message = "Subscription CREATED!"
            //Respond with code 202
            '*' { respond subscription, [status: ACCEPTED] }
        } else {
            result.success = false
            result.errorCode = 300
            result.message = "Subscription NOT created!"
        }

        println " Result: "
        println "  -success: "+result.success
        println "  -errorCode: "+result.errorCode
        println "  -message: "+result.message

        /*Respuesta en XML para AppDirect*/
        request.withFormat {
            xml { render result as XML }
        }
    }

   def edit(Subscription subscriptionInstance) {
        respond subscriptionInstance
    }

    @Transactional
    //Take in mind code 200
    def update(Subscription subscriptionInstance) {
        println "WS Update!!"
        if (subscriptionInstance == null) {
            notFound()
            return
        }

        if (subscriptionInstance.hasErrors()) {
            respond subscriptionInstance.errors, view: 'edit'
            return
        }

        subscriptionInstance.save flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'subscription.label', default: 'Subscription'), subscriptionInstance.id])
                redirect subscriptionInstance
            }
            '*' { respond subscriptionInstance, [status: OK] }
        }
    }

    //curl -i -X DELETE http://localhost:8080/GrailsAppDirect/api/subscriptions/17
    //Take in mind code 200
    @Transactional
    def delete(Subscription subscriptionInstance) {
        println "WS Delete!!"

        if (subscriptionInstance == null) {
            notFound()
            return
        }

        subscriptionInstance.delete flush: true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'subscription.label', default: 'Subscription'), subscriptionInstance.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'subscription.label', default: 'Subscription'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    /*TEST DATA*/

    /*Creation of Marketplace test data*/
    def buildMarketplace = {

        Marketplace marketplace = new Marketplace();
        marketplace.baseUrl = "https://acme.appdirect.com";
        marketplace.partner = "ACME"

        return  marketplace
    }

    /*Creation of User Creator test data*/
    def buildCreator = {

        Creator creator = new Creator();
        creator.firstName = "Walter"
        creator.lastName = "Hernandez"
        creator.openId = "https://plus.google.com/+WalterHernandez-id"
        creator.email = "walhernandez@gmail.com"
        creator.lang = "SP"
        creator.uuid = "a11a7918-bb43-4429-a256-f6d729c71033"

        return creator
    }

    /*Creation of Company test data*/
    def buildCompany = {

        Company company = new Company()
        company.name = "JCor"
        company.email = "support@jcor.com.ar"
        company.phoneNumber = "3512465522"
        company.website = "www.jcor.com.ar"
        company.uuid = "d15bb36e-5fb5-11e0-8c3c-00262d2cda03"

        return company
    }

    /*Creation of Account test data*/
    def buildAccount = {

        Account account = new Account()
        //At this point, we don't have an account identifier. We will have one for the future events
        account.identifier = ""
        account.status = AccountStatus.ACTIVE

        return account
    }

    /*Creation of Users test data*/
    def buildUsers = {subscriptions ->

        def users = []

        User user1 = new User();
        user1.firstName = "JCor User1 FirstName"
        user1.lastName = "JCor User1 LastName"
        user1.openId = "https://jcor.com.ar/id1"
        user1.subscriptionUser = subscriptions.get(0)

        User user2 = new User();
        user2.firstName = "JCor User2 FirstName"
        user2.lastName = "JCor User2 LastName"
        user2.openId = "https://jcor.com.ar/id2"
        user2.subscriptionUser = subscriptions.get(0)

        users.add(user1)
        users.add(user2)

        return users
    }

    /*Creation of Subscription test data*/
    def buildSubscription = {marketplace, account, company, creator, users, subscription ->

        subscription.subscriptionType = SubscriptionType.SUBSCRIPTION_ORDER
        subscription.marketplace = marketplace
        subscription.account = account
        subscription.company = company
        subscription.creator = creator
        users.each {
            subscription.addToUsers(it)
        }

        return subscription
    }
}
