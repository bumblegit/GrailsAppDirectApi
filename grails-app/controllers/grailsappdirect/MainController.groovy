package grailsappdirect

import grails.transaction.Transactional

@Transactional(readOnly = true)
class MainController {

    def index() {
        println "Starting application ..."
    }
}
