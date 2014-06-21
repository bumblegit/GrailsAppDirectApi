package grailsappdirect

import grails.rest.Resource

@Resource
class Subscription {

    SubscriptionType subscriptionType
    Marketplace marketplace
    Company company
    Order order
    Account account
    Creator creator
    Notice notice
    String flag

    static hasOne = [creator: Creator]
    static hasMany = [users: User]

    static mappedBy = [user: "subscriptionUser", creator: "subscriptionCreator"]

    static constraints = {
        subscriptionType nullable: false, blank: false
        marketplace nullable: false
        creator nullable: false
        company nullable: true
        //I can set it as not null when I'll use it.
        order nullable: true
        account nullable: true
        users nullable: true
        notice nullable: true, inList: Notice.values().toList()
        flag nullable: true, blank: true
    }

    String toString() {
        StringBuilder subscriptionMessage = new StringBuilder()
        subscriptionMessage.append("*Subscription*")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("- marketplace: ")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - partner: $marketplace.partner")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - partner: $marketplace.baseUrl")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("-----------------")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("- creator: ")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - firstName: $creator.firstName")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - lastName: $creator.lastName")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - email: $creator.email")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - uuid: $creator.uuid")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - openId: $creator.openId")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("-----------------")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("- company: ")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - name: $company.name")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - email: $company.email")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - uuid: $company.uuid")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - country: $company.country")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("-----------------")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("- order: ")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - editionCode: $order.editionCode")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - pricingDuration: $order.pricingDuration")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("    - items: $order.items")
        subscriptionMessage.append("\n")
        order.items.each {
            subscriptionMessage.append("        - order: $it.unit")
            subscriptionMessage.append("\n")
            subscriptionMessage.append("        - order: $it.quantity")
            subscriptionMessage.append("\n")
            subscriptionMessage.append("        -----------------")
            subscriptionMessage.append("\n")
        }
        subscriptionMessage.append("    - country: $company.country")
        subscriptionMessage.append("\n")
        subscriptionMessage.append("-----------------")
        subscriptionMessage.append("\n")
        subscriptionMessage.toString()
    }
}
