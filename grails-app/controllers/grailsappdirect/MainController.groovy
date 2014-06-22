package grailsappdirect

import grails.transaction.Transactional

@Transactional(readOnly = true)
class MainController {
    def openidService
    def index() {
        if (openidService.isNotLoggedIn(session)) {
            redirect(controller: "login", action: "/index")
        }
        println "Starting application ..."
    }
}
