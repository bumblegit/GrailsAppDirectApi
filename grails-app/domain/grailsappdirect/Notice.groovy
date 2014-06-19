package grailsappdirect

public enum Notice {
    DEACTIVATED("DEACTIVATED"), REACTIVATED("REACTIVATED"), CLOSED("CLOSED"), UPCOMING_INVOICE("UPCOMING_INVOICE")
    String id

    Notice(String id) {
        this.id = id
    }
}
