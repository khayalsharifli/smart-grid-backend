package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class EnergyAnalytics(
    val userId: String,
    val period: String,
    val totalConsumptionKwh: Double,
    val totalProductionKwh: Double,
    val avgDailyConsumption: Double,
    val avgDailyProduction: Double,
    val peakConsumptionKwh: Double,
    val dataPoints: List<EnergyDataPoint>
)

@Serializable
data class EnergyDataPoint(
    val timestamp: Long,
    val consumptionKwh: Double,
    val productionKwh: Double
)

@Serializable
data class CarbonFootprint(
    val userId: String,
    val totalCarbonSavedKg: Double,
    val treesEquivalent: Int,
    val greenEnergyPercentage: Double,
    val monthlyData: List<CarbonDataPoint>
)

@Serializable
data class CarbonDataPoint(
    val month: String,
    val carbonSavedKg: Double
)

@Serializable
data class EnergyForecast(
    val userId: String,
    val forecastPeriod: String,
    val predictedConsumptionKwh: Double,
    val predictedProductionKwh: Double,
    val confidence: Double,
    val dataPoints: List<ForecastDataPoint>
)

@Serializable
data class ForecastDataPoint(
    val timestamp: Long,
    val predictedKwh: Double,
    val lowerBound: Double,
    val upperBound: Double
)
