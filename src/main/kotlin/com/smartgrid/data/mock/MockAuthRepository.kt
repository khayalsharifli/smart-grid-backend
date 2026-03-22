package com.smartgrid.data.mock

import com.smartgrid.domain.model.User
import com.smartgrid.domain.model.UserRole
import com.smartgrid.domain.repository.AuthRepository
import com.smartgrid.util.ErrorMessages
import com.smartgrid.util.MockData
import com.smartgrid.util.MockIds

class MockAuthRepository : AuthRepository {

    private val users = mutableMapOf(
        MockIds.USER_001 to Pair(
            User(MockIds.USER_001, MockData.CONSUMER_EMAIL, MockData.CONSUMER_NAME, UserRole.CONSUMER, MockData.CONSUMER_WALLET, 1710000000000),
            MockData.CONSUMER_PASSWORD_HASH
        ),
        MockIds.USER_002 to Pair(
            User(MockIds.USER_002, MockData.PROSUMER_EMAIL, MockData.PROSUMER_NAME, UserRole.PROSUMER, MockData.PROSUMER_WALLET, 1710100000000),
            MockData.PROSUMER_PASSWORD_HASH
        ),
        MockIds.USER_003 to Pair(
            User(MockIds.USER_003, MockData.ADMIN_EMAIL, MockData.ADMIN_NAME, UserRole.ADMIN, MockData.ADMIN_WALLET, 1709900000000),
            MockData.ADMIN_PASSWORD_HASH
        ),
        MockIds.USER_004 to Pair(
            User(MockIds.USER_004, MockData.OPERATOR_EMAIL, MockData.OPERATOR_NAME, UserRole.GRID_OPERATOR, MockData.OPERATOR_WALLET, 1710200000000),
            MockData.OPERATOR_PASSWORD_HASH
        )
    )

    override suspend fun findByEmail(email: String): User? =
        users.values.find { it.first.email == email }?.first

    override suspend fun findById(id: String): User? =
        users[id]?.first

    override suspend fun create(user: User, passwordHash: String): User {
        users[user.id] = Pair(user, passwordHash)
        return user
    }

    override suspend fun update(user: User): User {
        val existing = users[user.id] ?: throw IllegalArgumentException(ErrorMessages.USER_NOT_FOUND)
        users[user.id] = Pair(user, existing.second)
        return user
    }

    override suspend fun linkWallet(userId: String, walletAddress: String): User {
        val existing = users[userId] ?: throw IllegalArgumentException(ErrorMessages.USER_NOT_FOUND)
        val updated = existing.first.copy(walletAddress = walletAddress)
        users[userId] = Pair(updated, existing.second)
        return updated
    }

    override suspend fun validatePassword(email: String, passwordHash: String): User? =
        users.values.find { it.first.email == email && it.second == passwordHash }?.first
}
