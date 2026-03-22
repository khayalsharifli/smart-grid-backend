package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class DashboardSummary(
    val userId: String,
    val role: UserRole,
    val currentConsumptionKw: Double,
    val currentProductionKw: Double,
    val todayConsumptionKwh: Double,
    val todayProductionKwh: Double,
    val ethBalance: Double,
    val energyTokenBalance: Double,
    val recentTransactions: List<Transaction>,
    val activeOffers: Int
)

@Serializable
data class DynamicPrice(
    val currentPricePerKwh: Double,
    val avgPricePerKwh: Double,
    val supplyKwh: Double,
    val demandKwh: Double,
    val trend: PriceTrend,
    val updatedAt: Long
)

@Serializable
enum class PriceTrend {
    RISING, FALLING, STABLE
}
