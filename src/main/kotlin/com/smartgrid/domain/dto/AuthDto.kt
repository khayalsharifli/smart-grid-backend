package com.smartgrid.domain.dto

import com.smartgrid.domain.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,
    val role: UserRole = UserRole.CONSUMER
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long,
    val userId: String
)

@Serializable
data class RefreshTokenRequest(
    val refreshToken: String
)

@Serializable
data class LinkWalletRequest(
    val walletAddress: String,
    val signature: String
)

@Serializable
data class UpdateProfileRequest(
    val name: String? = null,
    val email: String? = null
)
