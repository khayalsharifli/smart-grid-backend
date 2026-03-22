package com.smartgrid.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.Date

object JwtConfig {

    val algorithm: Algorithm = Algorithm.HMAC256(JwtConstants.SECRET)
    val issuer: String = JwtConstants.ISSUER
    val audience: String = JwtConstants.AUDIENCE

    fun generateToken(userId: String, role: String): String =
        JWT.create()
            .withAudience(JwtConstants.AUDIENCE)
            .withIssuer(JwtConstants.ISSUER)
            .withClaim(JwtConstants.CLAIM_USER_ID, userId)
            .withClaim(JwtConstants.CLAIM_ROLE, role)
            .withExpiresAt(Date(System.currentTimeMillis() + JwtConstants.ACCESS_TOKEN_VALIDITY_MS))
            .sign(algorithm)

    fun generateRefreshToken(userId: String): String =
        JWT.create()
            .withAudience(JwtConstants.AUDIENCE)
            .withIssuer(JwtConstants.ISSUER)
            .withClaim(JwtConstants.CLAIM_USER_ID, userId)
            .withClaim(JwtConstants.CLAIM_TYPE, JwtConstants.TYPE_REFRESH)
            .withExpiresAt(Date(System.currentTimeMillis() + JwtConstants.REFRESH_TOKEN_VALIDITY_MS))
            .sign(algorithm)

    fun validateRefreshToken(token: String): String? =
        try {
            val verifier = JWT.require(algorithm)
                .withAudience(JwtConstants.AUDIENCE)
                .withIssuer(JwtConstants.ISSUER)
                .build()
            val decoded = verifier.verify(token)
            if (decoded.getClaim(JwtConstants.CLAIM_TYPE).asString() == JwtConstants.TYPE_REFRESH) {
                decoded.getClaim(JwtConstants.CLAIM_USER_ID).asString()
            } else null
        } catch (e: Exception) {
            null
        }
}
