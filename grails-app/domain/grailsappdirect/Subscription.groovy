package grailsappdirect

import grails.rest.Resource

@Resource
class Subscription {

    SubscriptionType subscriptionType
    Marketplace marketplace
    Company company
    Account account
    Creator creator
    Notice notice

    static hasOne = [creator: Creator]
    static hasMany = [users: User]

    static mappedBy = [user: "subscriptionUser", creator: "subscriptionCreator"]

    static constraints = {
        subscriptionType nullable: false, blank: false
        marketplace nullable: false
        creator nullable: false
        company nullable: true
        account nullable: true
        users nullable: true
        notice nullable: true, inList: Notice.values().toList()
    }
}
