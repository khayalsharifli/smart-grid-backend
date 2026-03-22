package com.smartgrid.domain.repository

import com.smartgrid.domain.model.*

interface AnalyticsRepository {
    suspend fun getEnergyAnalytics(userId: String, period: String): EnergyAnalytics
    suspend fun getCarbonFootprint(userId: String): CarbonFootprint
    suspend fun getComparison(userId: String, period1: String, period2: String): Map<String, EnergyAnalytics>
}
