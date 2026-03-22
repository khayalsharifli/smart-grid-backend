package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class OfferStatus {
    ACTIVE, SOLD, CANCELLED, EXPIRED
}

@Serializable
enum class EnergySource {
    SOLAR, WIND, HYDRO, MIXED
}

@Serializable
data class EnergyOffer(
    val id: String,
    val sellerId: String,
    val sellerName: String,
    val energyAmountKwh: Double,
    val pricePerKwh: Double,
    val energySource: EnergySource,
    val status: OfferStatus,
    val expiresAt: Long,
    val createdAt: Long
)
