package grailsappdirect

class Creator {

    static belongsTo = [subscriptionCreator: Subscription]

    String firstName
    String lastName
    String lang
    String email
    String openId
    String uuid

    static constraints = {
        firstName blank: false, nullable: false
        lastName blank: false, nullable: false
        email email: true, unique: true
        openId unique: true, url: true
        uuid unique: true
        lang nullable: true
    }

}
