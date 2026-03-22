package com.smartgrid.routes

import com.smartgrid.domain.dto.*
import com.smartgrid.domain.model.SmartMeterData
import com.smartgrid.domain.service.MeterService
import com.smartgrid.plugins.userId
import com.smartgrid.util.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.meterRoutes(meterService: MeterService) {
    authenticate(JwtConstants.AUTH_NAME) {
        route(Routes.METERS) {
            get {
                val principal = call.principal<JWTPrincipal>()!!
                val meters = meterService.getMeters(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = meters))
            }

            post {
                val principal = call.principal<JWTPrincipal>()!!
                val request = call.receive<AddMeterRequest>()
                val meter = meterService.addMeter(principal.userId(), request)
                call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = meter))
            }

            delete(Routes.METER_BY_ID) {
                val id = call.parameters[Params.ID]
                    ?: return@delete call.respond(HttpStatusCode.BadRequest, ApiResponse<Nothing>(success = false, error = ErrorMessages.MISSING_ID))
                val deleted = meterService.deleteMeter(id)
                if (deleted) {
                    call.respond(HttpStatusCode.OK, ApiResponse<String>(success = true, message = SuccessMessages.METER_DELETED))
                } else {
                    call.respond(HttpStatusCode.NotFound, ApiResponse<Nothing>(success = false, error = ErrorMessages.OFFER_NOT_FOUND))
                }
            }

            get(Routes.METER_DATA) {
                val id = call.parameters[Params.ID]
                    ?: return@get call.respond(HttpStatusCode.BadRequest, ApiResponse<Nothing>(success = false, error = ErrorMessages.MISSING_ID))
                val data = meterService.getMeterData(id)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = data))
            }
        }
    }

    webSocket(Routes.METER_STREAM) {
        val json = Json { prettyPrint = false }
        while (true) {
            val mockData = SmartMeterData(
                meterId = MockIds.METER_001,
                userId = MockIds.USER_001,
                consumptionKwh = 0.5 + Math.random() * 2.0,
                productionKwh = Math.random() * 3.0,
                voltage = 220.0 + Math.random() * 10 - 5,
                frequency = 49.9 + Math.random() * 0.2,
                timestamp = System.currentTimeMillis()
            )
            send(Frame.Text(json.encodeToString(mockData)))
            delay(2000)
        }
    }
}
