package com.smartgrid.domain.service

import com.smartgrid.domain.dto.*
import com.smartgrid.domain.model.User
import com.smartgrid.domain.repository.AuthRepository
import com.smartgrid.util.ErrorMessages
import com.smartgrid.util.IdPrefix
import com.smartgrid.util.JwtConfig
import com.smartgrid.util.JwtConstants
import com.smartgrid.util.MockData
import java.util.UUID

class AuthService(private val repository: AuthRepository) {

    suspend fun register(request: RegisterRequest): AuthResponse {
        val existing = repository.findByEmail(request.email)
        if (existing != null) throw IllegalArgumentException(ErrorMessages.EMAIL_ALREADY_REGISTERED)

        val userId = "${IdPrefix.USER}${UUID.randomUUID().toString().take(8)}"
        val user = User(
            id = userId,
            email = request.email,
            name = request.name,
            role = request.role,
            createdAt = System.currentTimeMillis()
        )
        repository.create(user, hashPassword(request.password))

        return AuthResponse(
            accessToken = JwtConfig.generateToken(userId, request.role.name),
            refreshToken = JwtConfig.generateRefreshToken(userId),
            expiresIn = JwtConstants.EXPIRES_IN_SECONDS,
            userId = userId
        )
    }

    suspend fun login(request: LoginRequest): AuthResponse {
        val user = repository.validatePassword(request.email, hashPassword(request.password))
            ?: throw IllegalArgumentException(ErrorMessages.INVALID_CREDENTIALS)

        return AuthResponse(
            accessToken = JwtConfig.generateToken(user.id, user.role.name),
            refreshToken = JwtConfig.generateRefreshToken(user.id),
            expiresIn = JwtConstants.EXPIRES_IN_SECONDS,
            userId = user.id
        )
    }

    suspend fun refreshToken(request: RefreshTokenRequest): AuthResponse {
        val userId = JwtConfig.validateRefreshToken(request.refreshToken)
            ?: throw IllegalArgumentException(ErrorMessages.INVALID_REFRESH_TOKEN)

        val user = repository.findById(userId)
            ?: throw IllegalArgumentException(ErrorMessages.USER_NOT_FOUND)

        return AuthResponse(
            accessToken = JwtConfig.generateToken(user.id, user.role.name),
            refreshToken = JwtConfig.generateRefreshToken(user.id),
            expiresIn = JwtConstants.EXPIRES_IN_SECONDS,
            userId = user.id
        )
    }

    suspend fun getProfile(userId: String): User =
        repository.findById(userId) ?: throw IllegalArgumentException(ErrorMessages.USER_NOT_FOUND)

    suspend fun updateProfile(userId: String, request: UpdateProfileRequest): User {
        val user = repository.findById(userId) ?: throw IllegalArgumentException(ErrorMessages.USER_NOT_FOUND)
        val updated = user.copy(
            name = request.name ?: user.name,
            email = request.email ?: user.email
        )
        return repository.update(updated)
    }

    suspend fun linkWallet(userId: String, request: LinkWalletRequest): User =
        repository.linkWallet(userId, request.walletAddress)

    private fun hashPassword(password: String): String =
        "${MockData.HASH_PREFIX}$password"
}
