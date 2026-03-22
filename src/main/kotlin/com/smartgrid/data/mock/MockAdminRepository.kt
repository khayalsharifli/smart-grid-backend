package com.smartgrid.data.mock

import com.smartgrid.domain.dto.ContractInfo
import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.AdminRepository
import com.smartgrid.util.ErrorMessages
import com.smartgrid.util.IdPrefix
import com.smartgrid.util.MockData
import com.smartgrid.util.MockIds
import com.smartgrid.util.Network
import java.util.UUID

class MockAdminRepository(
    private val authRepository: MockAuthRepository
) : AdminRepository {

    private val contracts = mutableListOf(
        ContractInfo(MockData.CONTRACT_ENERGY_TOKEN, MockData.CONTRACT_ADDR_1, 1710000000000, Network.SEPOLIA),
        ContractInfo(MockData.CONTRACT_ENERGY_TRADING, MockData.CONTRACT_ADDR_2, 1710100000000, Network.SEPOLIA),
        ContractInfo(MockData.CONTRACT_METER_REGISTRY, MockData.CONTRACT_ADDR_3, 1710200000000, Network.SEPOLIA)
    )

    override suspend fun getAllUsers(): List<User> = listOf(
        authRepository.findById(MockIds.USER_001)!!,
        authRepository.findById(MockIds.USER_002)!!,
        authRepository.findById(MockIds.USER_003)!!,
        authRepository.findById(MockIds.USER_004)!!
    )

    override suspend fun changeUserRole(userId: String, role: UserRole): User {
        val user = authRepository.findById(userId) ?: throw IllegalArgumentException(ErrorMessages.USER_NOT_FOUND)
        val updated = user.copy(role = role)
        authRepository.update(updated)
        return updated
    }

    override suspend fun blockUser(userId: String, blocked: Boolean): User {
        val user = authRepository.findById(userId) ?: throw IllegalArgumentException(ErrorMessages.USER_NOT_FOUND)
        return user
    }

    override suspend fun getGridHealth(): GridHealth = GridHealth(
        totalNodes = 48,
        activeNodes = 45,
        totalLoadKw = 320.5,
        maxCapacityKw = 500.0,
        healthPercentage = 93.75,
        alerts = listOf(
            MockData.ALERT_HIGH_CONSUMPTION,
            MockData.ALERT_CONNECTION_DELAY
        )
    )

    override suspend fun emergencyShutdown(): Boolean = true

    override suspend fun getContracts(): List<ContractInfo> = contracts

    override suspend fun deployContract(name: String, args: List<String>): ContractInfo {
        val contract = ContractInfo(
            name = name,
            address = "${IdPrefix.HEX}${UUID.randomUUID().toString().replace("-", "").take(40)}",
            deployedAt = System.currentTimeMillis(),
            network = Network.SEPOLIA
        )
        contracts.add(contract)
        return contract
    }
}
