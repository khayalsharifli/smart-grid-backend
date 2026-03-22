package com.smartgrid.routes

import com.smartgrid.domain.dto.ApiResponse
import com.smartgrid.domain.service.LandingService
import com.smartgrid.util.Routes
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.landingRoutes(landingService: LandingService) {
    route(Routes.LANDING) {
        get {
            val data = landingService.getLandingPageData()
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
        }

        get(Routes.HERO) {
            val data = landingService.getHero()
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
        }

        get(Routes.FEATURES) {
            val data = landingService.getFeatures()
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
        }

        get(Routes.HOW_IT_WORKS) {
            val data = landingService.getHowItWorks()
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
        }

        get(Routes.PLATFORM_STATS) {
            val data = landingService.getStats()
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
        }

        get(Routes.CTA) {
            val data = landingService.getCta()
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
        }

        get(Routes.FOOTER) {
            val data = landingService.getFooter()
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
        }
    }
}
