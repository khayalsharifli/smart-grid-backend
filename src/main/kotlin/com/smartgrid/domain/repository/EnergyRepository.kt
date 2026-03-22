package com.smartgrid.domain.repository

import com.smartgrid.domain.model.*

interface EnergyRepository {
    suspend fun getConsumption(userId: String, period: String): List<EnergyDataPoint>
    suspend fun getProduction(userId: String, period: String): List<EnergyDataPoint>
    suspend fun getSummary(userId: String): DashboardSummary
    suspend fun getForecast(userId: String): EnergyForecast
}
