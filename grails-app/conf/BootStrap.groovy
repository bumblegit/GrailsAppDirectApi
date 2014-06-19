import grailsappdirect.*

class BootStrap {

    def init = { servletContext ->

        final SHOW_PRINTS = false
        final CREATE_TEST_DATA = true

        environments {
            production {
                //Add production Specific values.
            }
            development {
                if (CREATE_TEST_DATA) {
                    println "Creating Test Data! ..."
                    def marketplaces = buildMarketplace()
                    def creators = buildCreators()
                    def companies = buildCompanies()
                    def accounts = buildAccounts()

                    Subscription subscription1 = new Subscription();
                    Subscription subscription2 = new Subscription();
                    def subscriptions = []
                    subscriptions.add(subscription1)
                    subscriptions.add(subscription2)

                    def users = buildUsers(subscriptions)
                    subscriptions = buildSubscriptions(marketplaces, accounts, companies, creators, users, subscriptions)
                    saveSubscriptions(subscriptions)

                    println "Test Data created!"

                    if (SHOW_PRINTS) {
                        def printAllData = printAllData(marketplaces, accounts, companies, creators, users, subscriptions)
                        println printAllData
                    }
                }

            }
        }
    }

    /*Creation of Marketplace test data*/
    def buildMarketplace = {

        def marketplaces = []

        Marketplace marketplace1 = new Marketplace();
        marketplace1.baseUrl = "https://acme.appdirect.com";
        marketplace1.partner = "ACME"

        Marketplace marketplace2 = new Marketplace();
        marketplace2.baseUrl = "https://www.google.com/enterprise/marketplace";
        marketplace2.partner = "GOOGLE"

        marketplaces.add(marketplace1)
        marketplaces.add(marketplace2)

        return  marketplaces
    }

    /*Creation of User Creator test data*/
    def buildCreators = {

        def creators = []

        Creator creator1 = new Creator();
        creator1.firstName = "Walter"
        creator1.lastName = "Hernandez"
        creator1.openId = "https://plus.google.com/+WalterHernandez-id"
        creator1.email = "walhernandez@gmail.com"
        creator1.lang = "SP"
        creator1.uuid = "a11a7918-bb43-4429-a256-f6d729c71033"

        Creator creator2 = new Creator();
        creator2.firstName = "Dummy FirstName"
        creator2.lastName = "Dummy LastName"
        creator2.openId = "https://dummy.openid.com/dummy-id"
        creator2.email = "dummyEmail@gmail.com"
        creator2.lang = "EN"
        creator2.uuid = "a11a7918-bb43-4429-a256-f6d729c71034"

        creators.add(creator1)
        creators.add(creator2)

        return creators
    }

    /*Creation of Company test data*/
    def buildCompanies = {

        def companies = []

        Company company1 = new Company()
        company1.name = "JCor"
        company1.email = "support@jcor.com.ar"
        company1.phoneNumber = "3512465522"
        company1.website = "www.jcor.com.ar"
        company1.uuid = "d15bb36e-5fb5-11e0-8c3c-00262d2cda03"

        Company company2 = new Company()
        company2.name = "Google"
        company2.email = "support@google.com"
        company2.phoneNumber = "4444333221"
        company2.website = "www.google.com"
        company2.uuid = "d15bb36e-5fb5-11e0-8c3c-00262d2cda04"

        companies.add(company1)
        companies.add(company2)

        return companies
    }

    /*Creation of Account test data*/
    def buildAccounts = {

        def accounts = []

        Account account1 = new Account()
        account1.identifier = "JCorAccountID"
        account1.status = AccountStatus.ACTIVE

        Account account2 = new Account()
        account2.identifier = "GoogleAccountID"
        account2.status = AccountStatus.ACTIVE

        accounts.add(account1)
        accounts.add(account2)

        return accounts
    }

    /*Creation of Users test data*/
    def buildUsers = {subscriptions ->

        def users = []

        User user1 = new User();
        user1.firstName = "JCor User1 FirstName"
        user1.lastName = "JCor User1 LastName"
        user1.openId = "https://jcor.com.ar/id1"
        user1.subscriptionUser = subscriptions.get(0)

        User user2 = new User();
        user2.firstName = "JCor User2 FirstName"
        user2.lastName = "JCor User2 LastName"
        user2.openId = "https://jcor.com.ar/id2"
        user2.subscriptionUser = subscriptions.get(0)

        User user3 = new User();
        user3.firstName = "Google User FirstName"
        user3.lastName = "Google User LastName"
        user3.openId = "https://plus.google.com/+Dummy-id"
        user3.subscriptionUser = subscriptions.get(1)

        users.add(user1)
        users.add(user2)
        users.add(user3)

        return users
    }

    /*Creation of Subscription test data*/
    @SuppressWarnings("GroovyAssignabilityCheck")
    def buildSubscriptions = {marketplaces, accounts, companies, creators, users, subscriptions ->

        Subscription subscription1 = subscriptions.get(0)
        subscription1.subscriptionType = SubscriptionType.SUBSCRIPTION_ORDER
        subscription1.marketplace = marketplaces.get(0)
        subscription1.account = accounts.get(0)
        subscription1.company = companies.get(0)
        subscription1.creator = creators.get(0)
        subscription1.addToUsers(users.get(0))
        subscription1.addToUsers(users.get(1))

        Subscription subscription2 = subscriptions.get(1)
        subscription2.subscriptionType = SubscriptionType.SUBSCRIPTION_ORDER
        subscription2.marketplace = marketplaces.get(1)
        subscription2.account = accounts.get(1)
        subscription2.company = companies.get(1)
        subscription2.creator = creators.get(1)
        subscription2.addToUsers(users.get(2))

        subscriptions.add(subscription1)
        subscriptions.add(subscription2)

        return subscriptions
    }

    def saveSubscriptions = {subscriptions ->
        subscriptions.each {
            it.save(failOnError:true)
        }
    }

    def printAllData = {marketplaces, accounts, companies, creators, users, subscriptions ->
        println "========================================="
        println "MARKETPLACES: "
        println "========================================="
        marketplaces.each {
            println "-baseUrl: $it.baseUrl"
            println "-partner: $it.partner"
            println " ------------------------------------"
        }

        println "========================================="
        println "ACCOUNTS: "
        println "========================================="
        accounts.each {
            println "-identifier: $it.identifier"
            println "-status: $it.status"
            println " ------------------------------------"
        }

        println "========================================="
        println "COMPANIES: "
        println "========================================="
        companies.each {
            println "-uuid: $it.uuid"
            println "-email: $it.email"
            println "-name: $it.name"
            println "-phoneNumber: $it.phoneNumber"
            println "-website: $it.website"
            println " ------------------------------------"
        }

        println "========================================="
        println "CREATORS: "
        println "========================================="
        creators.each {
            println "-firstName: $it.firstName"
            println "-lastName: $it.lastName"
            println "-language: $it.lang"
            println "-email: $it.email"
            println "-openId: $it.openId"
            println "-uuid: $it.uuid"
            println " ------------------------------------"
        }

        println "========================================="
        println "USERS: "
        println "========================================="
        users.each {
            println "-firstName: $it.firstName"
            println "-lastName: $it.lastName"
            println "-language: $it.lang"
            println "-email: $it.email"
            println "-openId: $it.openId"
            println "-uuid: $it.uuid"
            println "-subscription: $it.subscriptionUser.company.name"
            println " ------------------------------------"
        }

        println "========================================="
        println "SUBSCRIPTIONS: "
        println "========================================="
        subscriptions.each {
            println "-type: $it.subscriptionType"
            println "-marketplace: $it.marketplace.partner"
            println "-creator: $it.creator.firstName $it.creator.lastName"
            println "-company: $it.company.name"
            println "-account: $it.account.identifier"
            println "-users: "
            it.users.each {user ->
                println "   -user: $user.openId"
                println " ------------------------------------"
            }
        }
    }

    def destroy = {
    }
}
