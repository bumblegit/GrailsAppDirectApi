package grailsappdirect

import grails.transaction.Transactional

@Transactional(readOnly = true)
class MainController {

    def openidService

    final String dummyOpenId="https://www.appdirect.com/openid/id/ec5d8eda-5cec-444d-9e30-125b6e4b67e2"

    def index() {
        //String openId = dummyOpenId;
        String openId = params.openId;
        String accountIdentifier = params.accountIdentifier;
        if (openidService.isNotLoggedIn(session)) {
            redirect(controller: "login", action: "/index")
        }
        println "Starting application ..."
    }
}
