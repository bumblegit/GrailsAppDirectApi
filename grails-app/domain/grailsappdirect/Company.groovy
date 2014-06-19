package grailsappdirect

class Company {

    String uuid
    String email
    String name
    String phoneNumber
    String website

    static belongsTo = [subscription: Subscription]

    static constraints = {
        name blank: false, nullable: false
        email email: true, unique: true
        uuid unique: true
        phoneNumber unique: true
        website unique: true
    }
}
