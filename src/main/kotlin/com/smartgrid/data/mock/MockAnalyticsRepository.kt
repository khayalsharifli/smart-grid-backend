package com.smartgrid.data.mock

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.AnalyticsRepository
import com.smartgrid.util.Defaults
import com.smartgrid.util.MockData

class MockAnalyticsRepository : AnalyticsRepository {

    override suspend fun getEnergyAnalytics(userId: String, period: String): EnergyAnalytics {
        val now = System.currentTimeMillis()
        val dayMs = 86_400_000L
        val days = when (period) {
            Defaults.PERIOD_WEEKLY -> 7
            Defaults.PERIOD_MONTHLY -> 30
            else -> 1
        }
        return EnergyAnalytics(
            userId = userId,
            period = period,
            totalConsumptionKwh = days * 18.5,
            totalProductionKwh = days * 12.3,
            avgDailyConsumption = 18.5,
            avgDailyProduction = 12.3,
            peakConsumptionKwh = 4.8,
            dataPoints = (0 until days).map { i ->
                EnergyDataPoint(
                    timestamp = now - (days - 1 - i) * dayMs,
                    consumptionKwh = 15.0 + Math.random() * 7.0,
                    productionKwh = 8.0 + Math.random() * 8.0
                )
            }
        )
    }

    override suspend fun getCarbonFootprint(userId: String): CarbonFootprint = CarbonFootprint(
        userId = userId,
        totalCarbonSavedKg = 156.8,
        treesEquivalent = 7,
        greenEnergyPercentage = 64.5,
        monthlyData = listOf(
            CarbonDataPoint(MockData.CARBON_MONTH_1, 48.2),
            CarbonDataPoint(MockData.CARBON_MONTH_2, 52.1),
            CarbonDataPoint(MockData.CARBON_MONTH_3, 56.5)
        )
    )

    override suspend fun getComparison(userId: String, period1: String, period2: String): Map<String, EnergyAnalytics> {
        val a1 = getEnergyAnalytics(userId, Defaults.PERIOD_MONTHLY)
        val a2 = a1.copy(
            totalConsumptionKwh = a1.totalConsumptionKwh * 1.1,
            totalProductionKwh = a1.totalProductionKwh * 0.9
        )
        return mapOf(period1 to a1, period2 to a2)
    }
}
