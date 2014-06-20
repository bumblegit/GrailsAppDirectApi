package grailsappdirect

class Company {

    String country
    String email
    String name
    String uuid
    String phoneNumber
    String website

    static belongsTo = [subscription: Subscription]

    static constraints = {
        country blank: false, nullable: false
        email email: true, unique: true
        name blank: false, nullable: false
        uuid unique: true
        phoneNumber unique: true
        website unique: true
    }
}
