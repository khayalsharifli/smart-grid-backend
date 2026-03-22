package com.smartgrid.routes

import com.smartgrid.domain.dto.*
import com.smartgrid.domain.service.AuthService
import com.smartgrid.plugins.userId
import com.smartgrid.util.JwtConstants
import com.smartgrid.util.Routes
import com.smartgrid.util.SuccessMessages
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(authService: AuthService) {
    route(Routes.AUTH) {
        post(Routes.REGISTER) {
            val request = call.receive<RegisterRequest>()
            val response = authService.register(request)
            call.respond(HttpStatusCode.Created, ApiResponse(success = true, data = response, message = SuccessMessages.REGISTRATION_SUCCESSFUL))
        }

        post(Routes.LOGIN) {
            val request = call.receive<LoginRequest>()
            val response = authService.login(request)
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = response))
        }

        post(Routes.REFRESH) {
            val request = call.receive<RefreshTokenRequest>()
            val response = authService.refreshToken(request)
            call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = response))
        }

        authenticate(JwtConstants.AUTH_NAME) {
            post(Routes.WALLET_LINK) {
                val principal = call.principal<JWTPrincipal>()!!
                val request = call.receive<LinkWalletRequest>()
                val user = authService.linkWallet(principal.userId(), request)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = user))
            }

            get(Routes.PROFILE) {
                val principal = call.principal<JWTPrincipal>()!!
                val user = authService.getProfile(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = user))
            }

            put(Routes.PROFILE) {
                val principal = call.principal<JWTPrincipal>()!!
                val request = call.receive<UpdateProfileRequest>()
                val user = authService.updateProfile(principal.userId(), request)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = user))
            }
        }
    }
}
