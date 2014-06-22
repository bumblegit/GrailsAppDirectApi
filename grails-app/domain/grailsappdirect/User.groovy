package grailsappdirect

class User {

    static belongsTo = [subscriptionUser: Subscription]

    EventType eventType
    String firstName
    String lastName
    String lang
    String email
    String openId
    String uuid
    Profile profile

    static constraints = {
        eventType nullable: false, blank: false
        firstName blank: false, nullable: false
        lastName blank: false, nullable: false
        email email: true, unique: true, nullable: true
        openId unique: true, url: true, nullable: true
        uuid unique: true, nullable: true
        lang nullable: true
        profile nullable: true
    }

}
