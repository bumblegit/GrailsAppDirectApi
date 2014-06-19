package grailsappdirect

class Profile {

    String companyDepartment
    String companyTitle
    String billingRate
    String zipCode
    String timeZone
    boolean appAdmin

    static constraints = {
        companyDepartment nullable: true
        companyTitle nullable: true
        billingRate nullable: true
        zipCode nullable: true
        timeZone nullable: true
        appAdmin nullable: true, inList: [Boolean.TRUE, Boolean.FALSE]
    }
}
