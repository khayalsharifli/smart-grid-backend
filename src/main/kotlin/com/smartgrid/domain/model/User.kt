package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    CONSUMER, PROSUMER, GRID_OPERATOR, ADMIN
}

@Serializable
data class User(
    val id: String,
    val email: String,
    val name: String,
    val role: UserRole,
    val walletAddress: String? = null,
    val createdAt: Long
)
