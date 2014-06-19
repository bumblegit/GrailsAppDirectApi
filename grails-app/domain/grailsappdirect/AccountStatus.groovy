package grailsappdirect

public enum AccountStatus {
    FREE_TRIAL("FREE_TRIAL"), FREE_TRIAL_EXPIRED("FREE_TRIAL_EXPIRED"), ACTIVE("ACTIVE"), SUSPENDED("SUSPENDED"), CANCELED("CANCELED")
    String id
    AccountStatus(String id) {
        this.id = id
    }
}
