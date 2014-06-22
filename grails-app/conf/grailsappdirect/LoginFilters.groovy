package grailsappdirect

import groovyx.net.http.URIBuilder

class LoginFilters {

    final def validMarketplaces= ['www.appdirect.com', 'www.appengine.google.com']

    def filters = {
        openIdLogin(controller:'openid', action:'login') {
            before = {
                String openidUrl = params.openid_url

                URIBuilder uriBuilder = new URIBuilder(openidUrl)
                String hostMarketplace = uriBuilder.getHost()

                def isInvalidMarket = !validMarketplaces.contains(hostMarketplace) as boolean
                if (isInvalidMarket) {
                    redirect(controller: 'login', action: 'notLogged')
                    return false
                }
            }

            after = { Map model ->
            }
            afterView = { Exception e ->

            }
        }
    }
}
