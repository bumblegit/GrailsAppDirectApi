package grailsappdirect

class Account {

    String identifier
    AccountStatus status

    static belongsTo = [subscription: Subscription]

    static constraints = {
        identifier nullable: false, blank: false
        status nullable: false, blank: false, inList: AccountStatus.values().toList()
    }

    /*identifier is the accountIdentifier sent to the AppDirect after the successful order subscription*/

}
