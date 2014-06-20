package grailsappdirect

class Order {

    String editionCode
    String pricingDuration

    static belongsTo = [subscription: Subscription]

    static hasMany = [items: OrderItem]

    static constraints = {
        //Null support everything because one Order can reach for every kind of events.
        //I need to figure out where are the real null attributes
        editionCode nullable: true
        pricingDuration nullable: true
    }
}
