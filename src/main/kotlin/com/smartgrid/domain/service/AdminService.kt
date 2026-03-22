package com.smartgrid.domain.service

import com.smartgrid.domain.dto.*
import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.AdminRepository

class AdminService(private val repository: AdminRepository) {

    suspend fun getAllUsers(): List<User> =
        repository.getAllUsers()

    suspend fun changeUserRole(userId: String, request: ChangeRoleRequest): User =
        repository.changeUserRole(userId, request.role)

    suspend fun blockUser(userId: String, request: BlockUserRequest): User =
        repository.blockUser(userId, request.blocked)

    suspend fun getGridHealth(): GridHealth =
        repository.getGridHealth()

    suspend fun emergencyShutdown(): Boolean =
        repository.emergencyShutdown()

    suspend fun getContracts(): List<ContractInfo> =
        repository.getContracts()

    suspend fun deployContract(request: DeployContractRequest): ContractInfo =
        repository.deployContract(request.contractName, request.constructorArgs)
}
