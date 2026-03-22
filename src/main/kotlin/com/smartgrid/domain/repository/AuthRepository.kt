package com.smartgrid.domain.repository

import com.smartgrid.domain.model.User

interface AuthRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: String): User?
    suspend fun create(user: User, passwordHash: String): User
    suspend fun update(user: User): User
    suspend fun linkWallet(userId: String, walletAddress: String): User
    suspend fun validatePassword(email: String, passwordHash: String): User?
}
