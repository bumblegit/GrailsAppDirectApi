package grailsappdirect

import grails.transaction.Transactional

@Transactional(readOnly = true)
class LoginController {

    def index(Integer max) {
        println "Index"
    }

    def loggedIn() {
        println "Logged!!!. Redirecting to the application ..."
        redirect(controller: "Main", action: "index")
    }

    def notLogged() {
        println "Not Logged!"
        chain action: '/index', model: [errorOpenID: true]
    }

}
