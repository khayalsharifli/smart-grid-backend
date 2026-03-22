package com.smartgrid.plugins

import com.smartgrid.domain.dto.ApiResponse
import com.smartgrid.util.ErrorMessages
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(
                HttpStatusCode.BadRequest,
                ApiResponse<Nothing>(success = false, error = cause.message)
            )
        }
        exception<Throwable> { call, _ ->
            call.respond(
                HttpStatusCode.InternalServerError,
                ApiResponse<Nothing>(success = false, error = ErrorMessages.INTERNAL_SERVER_ERROR)
            )
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(
                HttpStatusCode.NotFound,
                ApiResponse<Nothing>(success = false, error = ErrorMessages.RESOURCE_NOT_FOUND)
            )
        }
    }
}
