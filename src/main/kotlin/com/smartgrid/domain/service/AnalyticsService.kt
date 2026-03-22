package com.smartgrid.domain.service

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.AnalyticsRepository

class AnalyticsService(private val repository: AnalyticsRepository) {

    suspend fun getEnergyAnalytics(userId: String, period: String): EnergyAnalytics =
        repository.getEnergyAnalytics(userId, period)

    suspend fun getCarbonFootprint(userId: String): CarbonFootprint =
        repository.getCarbonFootprint(userId)

    suspend fun getComparison(userId: String, period1: String, period2: String): Map<String, EnergyAnalytics> =
        repository.getComparison(userId, period1, period2)
}
