package grailsappdirect

class OrderItem {

    Integer quantity
    String unit

    static belongsTo = [order: Order]

    static constraints = {
        quantity nullable: false
        unit nullable: false, blank: false
    }
}
