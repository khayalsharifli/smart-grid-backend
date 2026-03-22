package com.smartgrid.domain.repository

import com.smartgrid.domain.dto.ContractInfo
import com.smartgrid.domain.model.*

interface AdminRepository {
    suspend fun getAllUsers(): List<User>
    suspend fun changeUserRole(userId: String, role: UserRole): User
    suspend fun blockUser(userId: String, blocked: Boolean): User
    suspend fun getGridHealth(): GridHealth
    suspend fun emergencyShutdown(): Boolean
    suspend fun getContracts(): List<ContractInfo>
    suspend fun deployContract(name: String, args: List<String>): ContractInfo
}
