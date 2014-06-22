package grailsappdirect

import grails.converters.XML
import grails.rest.RestfulController
import grails.transaction.Transactional
import org.apache.commons.logging.LogFactory
import org.scribe.model.Response
import org.scribe.model.Token
import uk.co.desirableobjects.oauth.scribe.OauthService

import static org.springframework.http.HttpStatus.*

@Transactional(readOnly = true)
class UserController extends RestfulController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    private static final log = LogFactory.getLog("restService")

    final String URL_ENCODING = "UTF-8"

    OauthService oauthService

    UserController() {
        super(Subscription)
    }

    /*
curl -i -H "Accept: application/xml"  -H "Content-Type: application/xml" -X POST -d "" http://localhost:8080/GrailsAppDirectApi/api/users?eventUrl=https%3A%2F%2Fwww.appdirect.com%2FAppDirect%2Frest%2Fapi%2Fevents%2FdummyAssign
    */

    @Transactional
    def save() {

        log.info message(code: "user.assigning")
        println message(code: "user.assigning")

        boolean isValidURLEvent = validateEventUrl(params.eventUrl, EventType.USER_ASSIGNMENT)
        if (!isValidURLEvent) {return}

        String eventUrl = URLDecoder.decode(params.eventUrl as String, URL_ENCODING)
        log.info "eventUrlDecoded: $eventUrl"

        /*TODO implement STATELESS for dummy user assignments:
        *
        * http://info.appdirect.com/developers/docs/publication_and_maintenance/api_uptime_monitoring
        *
        * */

        Token token = getRequestToken(eventUrl)

        Response response = oauthService.getAppdirectResource(token, eventUrl)
        boolean isValidResponse = validateResponse(response)
        if (!isValidResponse) {return}

        User user = parseResponseInUserAssignment(response)

        boolean isValidUserBuilt = validateSubscriptionBuilt(user)
        if (!isValidUserBuilt) {return}

        println user
        log.info(user)

        boolean userSaved = user.save(flush: true, failOnError: true) as boolean

        if (userSaved) {
            println "user.subscriptionUser: "+user.subscriptionUser
            createResult(true, null, message(code: "user.validation.assigned"), OK, user.subscriptionUser.account.identifier)
        } else {
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "user.validation.not.assigned"), OK, null)
        }
    }

    /*
curl -i -X DELETE http://localhost:8080/GrailsAppDirectApi/api/users/6
  */

    @Transactional
    def delete(User userInstance) {
        log.info message(code: "user.deleting")

        if (userInstance == null) {
            respond userInstance, [status: NOT_FOUND]
        }

        userInstance?.delete(flush: true, failOnError:true)
        boolean userDeleted = userInstance?.id != null && !userInstance?.exists(userInstance?.id)

        if (userDeleted) {
            createResult(true, null, message(code: "user.validation.deleted"), OK, userInstance?.subscriptionUser?.company?.uuid)
        } else {
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "user.validation.not.deleted"), OK, userInstance?.subscriptionUser?.company?.uuid)
        }
    }

    private def validateSubscriptionBuilt = {subscription ->
        if (subscription == null || subscription.hasErrors()) {
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "subscription.validation.not.created"), OK, null)
            return false
        }
        return true
    }

    private def getRequestToken = {eventUrl ->
        final String tokenLastMarkReplacement = "/"
        final String tokenEmptyMarkReplacement = ""

        String tokenStr = eventUrl.reverse().find('\\w+')?.reverse()?.replace(tokenLastMarkReplacement, tokenEmptyMarkReplacement)

        log.info "tokenStr: $tokenStr"

        /*The OAuth plugin already validate the configuration*/
        def oauthConfig = grailsApplication.config.oauth
        String secret = oauthConfig?.providers?.appdirect?.secret

        Token token = new Token(tokenStr, secret)

        return token
    }

    private def validateEventUrl = {eventUrl, EventType eventType ->
        if (eventUrl == null || eventUrl.isEmpty()) {
            String msgParam=""
            if (eventType.equals(EventType.SUBSCRIPTION_ORDER)) {
                msgParam = message(code: "subscription.validation.not.created")
            } else if (eventType.equals(EventType.USER_ASSIGNMENT)) {
                msgParam = message(code: "user.validation.not.assigned")
            }
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "subscription.validation.param.url", args: [msgParam]), OK, null)
            return false
        }
        return true
    }

    private def parseResponseInUserAssignment = {response ->
        /*PARSING process*/

        String responseAsXmlText = response.getBody()

        /* First XML Level */
        def eventNode = new XmlParser().parseText(responseAsXmlText as String)
        def creatorNode = eventNode.creator
        def accountNode = eventNode.payload.account
        def userNode = eventNode.payload.user
        def typeNode = eventNode.type

        /*** User ***/

        User user = new User()
        String typeNodeText = typeNode.text()
        user.eventType = EventType.valueOf(typeNodeText)

        String uuid = creatorNode.uuid.text()
        println "uuid: $uuid"
        Creator creator = Creator.findByUuid(uuid)
        boolean creatorExists = validateCreatorExists(creator)
        if (!creatorExists) {return}
        Subscription subscription = creator.subscriptionCreator

        /*** Payload ***/

        //Account
        Account account = new Account()
        account.identifier = accountNode.accountIdentifier.text()
        account.status = accountNode.status.text()
        account.subscription = subscription

        /*** User ***/

        user.email = userNode.email.text()
        user.firstName = userNode.firstName.text()
        user.lastName = userNode.lastName.text()
        user.lang = userNode.language.text()
        user.openId = userNode.openId.text()
        user.uuid = userNode.uuid.text()
        user.subscriptionUser = subscription

        /*** Subscription assignments***/

        subscription.flag = eventNode.flag.text()
        subscription.account = account
        subscription.addToUsers(user)

        return user

    }

    def validateCreatorExists = {creator ->
        if (creator == null) {
            String paramMsg = message(code: "user.validation.not.assigned")
            createResult(false, ErrorCode.UNKNOWN_ERROR, message(code: "user.validation.creator.exists", args: [paramMsg]), OK, null)
            return false
        }
        return true
    }

    private validateResponse = {response ->
        if (response == null) {
            createResult(false, ErrorCode.UNAUTHORIZED, message(code: "oauth.unauthorized"), UNAUTHORIZED, null)
            return false
        }
        if (!response.isSuccessful()) {
            createResult(false, ErrorCode.INVALID_RESPONSE, message(code: "oauth.response.invalid"), NO_CONTENT, null)
            return false
        }
        return true
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
        if (result.errorCode != null) {
            log.info result
        } else {
            log.error result
        }
    }

}
