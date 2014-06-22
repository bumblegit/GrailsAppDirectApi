class UrlMappings {

	static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'login', action:"/index")
        "500"(view:'/error')
        "/api/subscriptions" (resources: 'subscription')
        "/api/subscriptions/*" (resources: 'subscription/*')
        "/api/users" (resources: 'user')
        "/api/users/*" (resources: 'user/*')
	}
}
