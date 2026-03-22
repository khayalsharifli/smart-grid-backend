package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class TransactionStatus {
    PENDING, CONFIRMED, DELIVERED, SETTLED, FAILED
}

@Serializable
enum class TransactionType {
    ENERGY_PURCHASE, TOKEN_TRANSFER, REWARD_CLAIM
}

@Serializable
data class Transaction(
    val id: String,
    val txHash: String,
    val buyerId: String,
    val sellerId: String,
    val offerId: String,
    val energyAmountKwh: Double,
    val totalPrice: Double,
    val type: TransactionType,
    val status: TransactionStatus,
    val createdAt: Long
)
