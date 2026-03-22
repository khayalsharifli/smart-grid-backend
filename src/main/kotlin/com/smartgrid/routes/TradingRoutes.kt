package com.smartgrid.routes

import com.smartgrid.domain.dto.*
import com.smartgrid.domain.service.TradingService
import com.smartgrid.plugins.userId
import com.smartgrid.util.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.tradingRoutes(tradingService: TradingService) {
    authenticate(JwtConstants.AUTH_NAME) {
        route(Routes.TRADING) {
            get(Routes.OFFERS) {
                val offers = tradingService.getActiveOffers()
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = offers))
            }

            get(Routes.OFFER_BY_ID) {
                val id = call.parameters[Params.ID]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, ApiResponse<Nothing>(success = false, error = ErrorMessages.MISSING_ID))
                val offer = tradingService.getOfferById(id)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = offer))
            }

            post(Routes.OFFERS) {
                val principal = call.principal<JWTPrincipal>()!!
                val request = call.receive<CreateOfferRequest>()
                val offer = tradingService.createOffer(principal.userId(), principal.userId(), request)
                call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = offer))
            }

            delete(Routes.OFFER_BY_ID) {
                val id = call.parameters[Params.ID]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ApiResponse<Nothing>(success = false, error = ErrorMessages.MISSING_ID))
                val deleted = tradingService.deleteOffer(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK, ApiResponse<String>(success = true, message = SuccessMessages.OFFER_CANCELLED))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse<Nothing>(success = false, error = ErrorMessages.OFFER_NOT_FOUND))
                }
            }

            post(Routes.BUY) {
                val principal = call.principal<JWTPrincipal>()!!
                val request = call.receive<BuyEnergyRequest>()
                val transaction = tradingService.buyEnergy(principal.userId(), request)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = transaction))
            }

            get(Routes.HISTORY) {
                val principal = call.principal<JWTPrincipal>()!!
                val history = tradingService.getTradeHistory(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = history))
            }

            get(Routes.PRICE) {
                val price = tradingService.getDynamicPrice()
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = price))
            }
        }
    }
}
