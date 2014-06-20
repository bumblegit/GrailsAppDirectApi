package grailsappdirect

import grails.converters.XML
import grails.rest.RestfulController
import grails.transaction.Transactional
import static org.springframework.http.HttpStatus.*
import org.scribe.model.Token
import uk.co.desirableobjects.oauth.scribe.OauthService

@Transactional(readOnly = true)
class SubscriptionController extends RestfulController{

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    OauthService oauthService

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
        curl -i -H "Accept: application/xml"  -H "Content-Type: application/xml" -X POST -d "" http://localhost:8080/GrailsAppDirectApi/api/subscriptions?eventUrl=https%3A%2F%2Fwww.appdirect.com%2Frest%2Fapi%2Fevents%2F12345

    URL's
    In AppDirect configuration:
        http://localhost:8080/GrailsAppDirectApi/api/subscriptions?eventUrl={eventUrl}
    In GrailsAppDirect
        http://localhost:8080/GrailsAppDirectApi/api/subscriptions?eventUrl=https%3A%2F%2Fwww.appdirect.com%2Frest%2Fapi%2Fevents%2F12345

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
        Subscription subscription = new Subscription()

            /*we need to parse the XML and fetch it into Subscription's objects */
            /*Build for coming subscription. Once twe have de response for the call to appdirect data,*/
            Marketplace marketplace = buildMarketplace()
            Company company = buildCompany()
            Creator creator = buildCreator()
            Account account = buildAccount()
            def users = buildUsers(subscription)
            subscription = buildSubscription(marketplace, account, company, creator, users, subscription)

        /* Authorization and fetch the data */

        //Get the URL param

        String eventUrl

        /*TODO use the following convention
        "https%3A%2F%2Fwww.appdirect.com%2Frest%2Fapi%2Fevents%2F12345"
        eventUrl = params.eventUrl
        */
        eventUrl = "https://www.appdirect.com/AppDirect/rest/api/events/dummyOrder"

        final String tokenLastMarkReplacement = "/"
        final String tokenEmptyMarkReplacement = ""

        /*TODO use this option after implement the encoding convention for the URL
        * tokenLastMarkReplacement = "2F"
        * */

        def tokenStr = eventUrl.reverse().find('\\w+').reverse().replace(tokenLastMarkReplacement, tokenEmptyMarkReplacement)
        println "tokenStr: $tokenStr"

        /*TODO remove secret?, it's already on config and is for provider, not for token*/
        Token token = new Token(tokenStr, "secret")

        /*TODO if is not a valid authorization, then return SC_UNAUTHORIZED(401) respond*/

        def response = oauthService.getAppdirectResource(token, eventUrl)

        String responseAsXmlText = response.getBody()

        /*PARSING process*/

        /* First XML Level */
        def eventNode = new XmlParser().parseText(responseAsXmlText)
        def creatorNode = eventNode.creator
        def marketplaceNode = eventNode.marketplace
        def payloadNode = eventNode.payload
        def returnUrlNode = eventNode.returnUrl
        def typeNode = eventNode.type

        println "Email: "+creatorNode.email.text()
        println "Partner: "+marketplaceNode.partner.text()
        println "URL: "+returnUrlNode.text()
        println "Type: "+typeNode.text()

        /*Result creation*/
        Result result = new Result()


        /*TODO adapt with the real subscription*/
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

        boolean subscriptionSaved = subscription.save(flush: true, failOnError:true);
        //boolean subscriptionSaved = false

        if (subscriptionSaved) {
            result.success = true
            result.errorCode = null
            //We are going to use the company's UUID as accountIdentifier. We will have it available for future events
            result.accountIdentifier = subscription.company.uuid
            result.message = "Subscription CREATED!"
            //Respond with code 200
            respond subscription, [status: OK]
        } else {
            result.success = false
            result.errorCode = 300
            result.message = "Subscription NOT created!"
            respond subscription, [status: OK]
            /*According to AppDirect documentation, we should always return a HTTPStatus 200
            * The differences are in the errorCode
            * */
        }

        println " Result: "
        println "  -success: "+result.success
        println "  -errorCode: "+result.errorCode
        println "  -message: "+result.message

        /*XML response for AppDirect*/
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

        '*' { render status: NO_CONTENT }
    }

    protected void notFound() {
        '*' { render status: NO_CONTENT }
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
