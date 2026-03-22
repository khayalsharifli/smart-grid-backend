package com.smartgrid.domain.dto

import com.smartgrid.domain.model.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class ChangeRoleRequest(
    val role: UserRole
)

@Serializable
data class BlockUserRequest(
    val blocked: Boolean,
    val reason: String? = null
)

@Serializable
data class DeployContractRequest(
    val contractName: String,
    val constructorArgs: List<String> = emptyList()
)

@Serializable
data class ContractInfo(
    val name: String,
    val address: String,
    val deployedAt: Long,
    val network: String
)
