package grailsappdirect

import grails.converters.XML
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.scribe.model.Token
import uk.co.desirableobjects.oauth.scribe.OauthService

import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

@Transactional(readOnly = true)
class SubscriptionController extends RestfulController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    final String URL_ENCODING = "UTF-8"

    OauthService oauthService

    SubscriptionController() {
        super(Subscription)
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Subscription.list(params), model: [subscriptionInstanceCount: Subscription.count()]
    }

    def show(Subscription subscriptionInstance) {
        respond subscriptionInstance
    }

    /*
    curl -i -H "Accept: application/xml"  -H "Content-Type: application/xml" -X POST -d "" http://localhost:8080/GrailsAppDirectApi/api/subscriptions?eventUrl=https%3A%2F%2Fwww.appdirect.com%2FAppDirect%2Frest%2Fapi%2Fevents%2FdummyOrder
    */

    @Transactional
    def save() {

        println message(code: "subscription.creating")

        boolean isValidURLEvent = validateEventUrl(params.eventUrl)
        if (!isValidURLEvent) {return}

        String eventUrl = URLDecoder.decode(params.eventUrl as String, URL_ENCODING)
        println "eventUrlDecoded: $eventUrl"

        Token token = getToken(eventUrl)

        Subscription subscription = parseResponseInSubscription(token, eventUrl)

        boolean isValidSubscriptionBuilt = validateSubscriptionBuilt as boolean
        if (!isValidSubscriptionBuilt) {return}

        println subscription

        boolean subscriptionSaved = subscription.save(flush: true, failOnError:true) as boolean

        if (subscriptionSaved) {
            String accountIdentifier = subscription.company.uuid
            createResult(true, null, message(code: "subscription.validation.created"), OK, accountIdentifier)
        } else {
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "subscription.validation.not.created"), OK, null)
        }
    }

    /*
    curl -i -X DELETE http://localhost:8080/GrailsAppDirectApi/api/subscriptions/6
    */

    @Transactional
    def delete(Subscription subscriptionInstance) {
        println message(code: "subscription.deleting")

        if (subscriptionInstance == null) {
            respond subscriptionInstance, [status: NOT_FOUND]
        }

        subscriptionInstance?.delete(flush: true, failOnError:true)
        boolean subscriptionDeleted = subscriptionInstance?.id != null && !subscriptionInstance?.exists(subscriptionInstance?.id)

        if (subscriptionDeleted) {
            //We are going to use the company's UUID as accountIdentifier. We will have it available for future events
            createResult(true, null, message(code: "subscription.validation.deleted"), OK, subscriptionInstance?.company?.uuid)
        } else {
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "subscription.validation.not.deleted"), OK, subscriptionInstance?.company?.uuid)
        }
    }

    private def validateSubscriptionBuilt = {subscription ->
        if (subscription == null || subscription.hasErrors()) {
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "subscription.validation.not.created"), OK, null)
        }
    }

    private def getToken = {eventUrl ->
        final String tokenLastMarkReplacement = "/"
        final String tokenEmptyMarkReplacement = ""

        String tokenStr = eventUrl.reverse().find('\\w+')?.reverse()?.replace(tokenLastMarkReplacement, tokenEmptyMarkReplacement)

        println "tokenStr: $tokenStr"

        /*The OAuth plugin already validate the configuration*/
        def oauthConfig = grailsApplication.config.oauth
        String secret = oauthConfig?.providers?.appdirect?.secret

        Token token = new Token(tokenStr, secret)

        return token
    }

    private def validateEventUrl = {eventUrl ->
        if (eventUrl == null || eventUrl.isEmpty()) {
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "subscription.validation.param.url"), OK, null)
            return false
        }
        return true
    }

    private def parseResponseInSubscription = {Token token, eventUrl ->
        /*PARSING process*/

        def response = oauthService.getAppdirectResource(token, eventUrl)

        String responseAsXmlText = response.getBody()

        /* First XML Level */
        def eventNode = new XmlParser().parseText(responseAsXmlText as String)
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

        return subscription

    }

    private def createResult = {success, errorCode, message, status, accountIdentifier ->
        Result result = new Result()
        result.success = success
        result.errorCode = errorCode
        result.message = message
        result.accountIdentifier = accountIdentifier
        respond result, [status: status]
        request.withFormat {
            xml { render result as XML }
        }
        println result
    }

}
