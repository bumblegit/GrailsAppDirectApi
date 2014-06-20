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

        /* Get the URL param */

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
        /*Authorization and response*/

        def response = oauthService.getAppdirectResource(token, eventUrl)

        String responseAsXmlText = response.getBody()

        /*PARSING process*/

        /* First XML Level */
        def eventNode = new XmlParser().parseText(responseAsXmlText)
        def creatorNode = eventNode.creator
        def marketplaceNode = eventNode.marketplace
        def payloadNode = eventNode.payload
        def companyNode = payloadNode.company
        def orderNode = payloadNode.order
        //def returnUrlNode = eventNode.returnUrl
        def typeNode = eventNode.type

        /*** Subscription ***/

        Subscription subscription = new Subscription()
        String typeNodeText = typeNode.text()
        subscription.subscriptionType = SubscriptionType.valueOf(typeNodeText)

        /*** Creator ***/

        Creator creator = new Creator()
        creator.email = creatorNode.email.text()
        creator.firstName = creatorNode.firstName.text()
        creator.lastName = creatorNode.lastName.text()
        creator.lang = creatorNode.language.text()
        creator.openId = creatorNode.openId.text()
        creator.uuid = creatorNode.uuid.text()
        creator.subscriptionCreator = subscription

        /*** Marketplace ***/

        Marketplace marketplace = new Marketplace()
        marketplace.partner = marketplaceNode.partner.text()
        marketplace.baseUrl = marketplaceNode.baseUrl.text()
        marketplace.subscription = subscription

        /*** Payload ***/

        //Company
        Company company = new Company()
        company.country = companyNode.country.text()
        company.email = companyNode.email.text()
        company.name = companyNode.name.text()
        company.uuid = companyNode.uuid.text()
        company.phoneNumber = companyNode.phoneNumber.text()
        company.website = companyNode.website.text()
        company.subscription = subscription

        //Order
        Order order = new Order()
        order.editionCode = orderNode.editionCode.text()
        order.pricingDuration = orderNode.pricingDuration.text()
        orderNode.item.findAll{
            OrderItem item = new OrderItem()
            item.quantity = it.quantity.text() as Integer
            item.unit = it.unit.text()
            item.order = order
            order.addToItems(item)
        }
        order.subscription = subscription

        /*** Subscription assignments***/

        subscription.creator = creator
        subscription.marketplace = marketplace
        subscription.company = company
        subscription.order = order

        /* Result creation */
        Result result = new Result()

        /*TODO adapt with the real subscription*/
        if (subscription == null || subscription.hasErrors()) {
            result.success = false
            result.errorCode = ErrorCode.UNKNOWN_ERROR
            result.message = "Subscription NOT created!"
        }

        /*Prints for subscription built */
        println "create - subscription: "
        println " -marketplace: "+subscription.marketplace.partner
        println " -creator: "+subscription.creator.firstName+" "+subscription.creator.lastName
        println " -company: "+subscription.company.name
        println " -Order: $subscription.order.editionCode"
        subscription.order.items.each {
            println "   -item-unit: $it.unit"
            println "   -item-quantity: $it.quantity"
            println "----------"
        }

        boolean subscriptionSaved = subscription.save(flush: true, failOnError:true) as boolean;

        if (subscriptionSaved) {
            result.success = true
            result.errorCode = null
            //We are going to use the company's UUID as accountIdentifier. We will have it available for future events
            result.accountIdentifier = subscription.company.uuid
            result.message = "Subscription CREATED!"
            //Respond with code 200
            respond result, [status: OK]
        } else {
            result.success = false
            result.errorCode = 300
            result.message = "Subscription NOT created!"
            respond result, [status: OK]
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
        respond result, [status: OK]

    }

    //curl -i -X DELETE http://localhost:8080/GrailsAppDirectApi/api/subscriptions/6
    //Take in mind code 200
    @Transactional
    def delete(Subscription subscriptionInstance) {
        println "WS Delete!!"

        /* Result creation */
        Result result = new Result()

        if (subscriptionInstance == null) {
            respond subscriptionInstance, [status: NOT_FOUND]
        }

        boolean subscriptionDeleted = subscriptionInstance.delete(flush: true, failOnError:true) as boolean;

        if (subscriptionDeleted) {
            result.success = true
            result.errorCode = null
            //We are going to use the company's UUID as accountIdentifier. We will have it available for future events
            result.accountIdentifier = subscription.company.uuid
            result.message = "Subscription DELETED!"
            //Respond with code 200
            respond result, [status: OK]
        } else {
            result.success = false
            result.errorCode = 300
            result.message = "Subscription NOT deleted!"
            respond result, [status: OK]
            /*According to AppDirect documentation, we should always return a HTTPStatus 200
            * The differences are in the errorCode
            * */
        }

        request.withFormat {
            xml { render subscriptionInstance as XML }
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
