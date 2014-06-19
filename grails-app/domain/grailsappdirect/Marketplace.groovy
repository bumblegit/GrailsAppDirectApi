package grailsappdirect

class Marketplace {

    String baseUrl
    String partner

    static belongsTo = [subscription: Subscription]

    static constraints = {
        baseUrl url: true, nullable: false, blank: false
        partner unique: true, nullable: false, blank: false
    }
}
