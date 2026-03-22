package com.smartgrid.data.mock

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.EnergyRepository
import com.smartgrid.util.Defaults
import com.smartgrid.util.MockData
import com.smartgrid.util.MockIds

class MockEnergyRepository : EnergyRepository {

    override suspend fun getConsumption(userId: String, period: String): List<EnergyDataPoint> {
        val now = System.currentTimeMillis()
        val hourMs = 3_600_000L
        return (0..23).map { i ->
            EnergyDataPoint(
                timestamp = now - (23 - i) * hourMs,
                consumptionKwh = (1.5 + Math.random() * 3.0),
                productionKwh = 0.0
            )
        }
    }

    override suspend fun getProduction(userId: String, period: String): List<EnergyDataPoint> {
        val now = System.currentTimeMillis()
        val hourMs = 3_600_000L
        return (0..23).map { i ->
            val hour = i % 24
            val solar = if (hour in 6..18) (Math.random() * 5.0) else 0.0
            EnergyDataPoint(
                timestamp = now - (23 - i) * hourMs,
                consumptionKwh = 0.0,
                productionKwh = solar
            )
        }
    }

    override suspend fun getSummary(userId: String): DashboardSummary {
        return DashboardSummary(
            userId = userId,
            role = UserRole.CONSUMER,
            currentConsumptionKw = 2.4,
            currentProductionKw = 0.0,
            todayConsumptionKwh = 18.7,
            todayProductionKwh = 0.0,
            ethBalance = 0.15,
            energyTokenBalance = 245.0,
            recentTransactions = listOf(
                Transaction(MockIds.TX_001, MockData.MOCK_TX_HASH_1, MockIds.USER_001, MockIds.USER_002, MockIds.OFFER_001, 10.0, 2.5, TransactionType.ENERGY_PURCHASE, TransactionStatus.SETTLED, System.currentTimeMillis() - 86400000),
                Transaction(MockIds.TX_002, MockData.MOCK_TX_HASH_2, MockIds.USER_001, MockIds.USER_002, MockIds.OFFER_002, 5.0, 1.25, TransactionType.ENERGY_PURCHASE, TransactionStatus.CONFIRMED, System.currentTimeMillis() - 43200000)
            ),
            activeOffers = 3
        )
    }

    override suspend fun getForecast(userId: String): EnergyForecast {
        val now = System.currentTimeMillis()
        val hourMs = 3_600_000L
        return EnergyForecast(
            userId = userId,
            forecastPeriod = Defaults.FORECAST_PERIOD_24H,
            predictedConsumptionKwh = 22.5,
            predictedProductionKwh = 15.0,
            confidence = 0.87,
            dataPoints = (0..23).map { i ->
                val base = 1.5 + Math.random() * 2.0
                ForecastDataPoint(
                    timestamp = now + i * hourMs,
                    predictedKwh = base,
                    lowerBound = base * 0.8,
                    upperBound = base * 1.2
                )
            }
        )
    }
}
