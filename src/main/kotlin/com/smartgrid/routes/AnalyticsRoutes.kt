package com.smartgrid.routes

import com.smartgrid.domain.dto.ApiResponse
import com.smartgrid.domain.service.AnalyticsService
import com.smartgrid.plugins.userId
import com.smartgrid.util.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.analyticsRoutes(analyticsService: AnalyticsService) {
    authenticate(JwtConstants.AUTH_NAME) {
        route(Routes.ANALYTICS) {
            get(Routes.ENERGY) {
                val principal = call.principal<JWTPrincipal>()!!
                val period = call.queryParameters[Params.PERIOD] ?: Defaults.PERIOD_MONTHLY
                val data = analyticsService.getEnergyAnalytics(principal.userId(), period)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }

            get(Routes.CARBON) {
                val principal = call.principal<JWTPrincipal>()!!
                val data = analyticsService.getCarbonFootprint(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }

            get(Routes.COMPARISON) {
                val principal = call.principal<JWTPrincipal>()!!
                val period1 = call.queryParameters[Params.PERIOD1] ?: Defaults.DEFAULT_PERIOD1
                val period2 = call.queryParameters[Params.PERIOD2] ?: Defaults.DEFAULT_PERIOD2
                val data = analyticsService.getComparison(principal.userId(), period1, period2)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }
        }
    }
}
