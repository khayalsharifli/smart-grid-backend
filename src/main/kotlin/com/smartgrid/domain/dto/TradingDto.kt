package com.smartgrid.domain.dto

import com.smartgrid.domain.model.EnergySource
import kotlinx.serialization.Serializable

@Serializable
data class CreateOfferRequest(
    val energyAmountKwh: Double,
    val pricePerKwh: Double,
    val energySource: EnergySource,
    val durationHours: Int
)

@Serializable
data class BuyEnergyRequest(
    val offerId: String,
    val buyerWalletAddress: String
)
