package com.smartgrid.routes

import com.smartgrid.domain.dto.ApiResponse
import com.smartgrid.domain.service.EnergyService
import com.smartgrid.plugins.userId
import com.smartgrid.util.Defaults
import com.smartgrid.util.JwtConstants
import com.smartgrid.util.Params
import com.smartgrid.util.Routes
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.energyRoutes(energyService: EnergyService) {
    authenticate(JwtConstants.AUTH_NAME) {
        route(Routes.ENERGY) {
            get(Routes.CONSUMPTION) {
                val principal = call.principal<JWTPrincipal>()!!
                val period = call.queryParameters[Params.PERIOD] ?: Defaults.PERIOD_DAILY
                val data = energyService.getConsumption(principal.userId(), period)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }

            get(Routes.PRODUCTION) {
                val principal = call.principal<JWTPrincipal>()!!
                val period = call.queryParameters[Params.PERIOD] ?: Defaults.PERIOD_DAILY
                val data = energyService.getProduction(principal.userId(), period)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }

            get(Routes.SUMMARY) {
                val principal = call.principal<JWTPrincipal>()!!
                val data = energyService.getSummary(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }

            get(Routes.FORECAST) {
                val principal = call.principal<JWTPrincipal>()!!
                val data = energyService.getForecast(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }
        }
    }
}
