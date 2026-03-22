package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class WalletInfo(
    val userId: String,
    val walletAddress: String,
    val ethBalance: Double,
    val energyTokenBalance: Double,
    val fiatEquivalentAzn: Double
)

@Serializable
data class Reward(
    val id: String,
    val userId: String,
    val amount: Double,
    val reason: String,
    val claimed: Boolean,
    val createdAt: Long
)
