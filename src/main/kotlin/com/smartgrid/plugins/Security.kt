package com.smartgrid.plugins

import com.smartgrid.util.ErrorMessages
import com.smartgrid.util.JwtConfig
import com.smartgrid.util.JwtConstants
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt(JwtConstants.AUTH_NAME) {
            realm = JwtConstants.REALM
            verifier(
                com.auth0.jwt.JWT
                    .require(JwtConfig.algorithm)
                    .withAudience(JwtConfig.audience)
                    .withIssuer(JwtConfig.issuer)
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(JwtConfig.audience)) {
                    JWTPrincipal(credential.payload)
                } else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, mapOf("error" to ErrorMessages.TOKEN_INVALID_OR_EXPIRED))
            }
        }
    }
}

fun JWTPrincipal.userId(): String =
    payload.getClaim(JwtConstants.CLAIM_USER_ID).asString()

fun JWTPrincipal.userRole(): String =
    payload.getClaim(JwtConstants.CLAIM_ROLE).asString()
