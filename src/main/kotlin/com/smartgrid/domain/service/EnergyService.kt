package com.smartgrid.domain.service

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.EnergyRepository

class EnergyService(private val repository: EnergyRepository) {

    suspend fun getConsumption(userId: String, period: String): List<EnergyDataPoint> =
        repository.getConsumption(userId, period)

    suspend fun getProduction(userId: String, period: String): List<EnergyDataPoint> =
        repository.getProduction(userId, period)

    suspend fun getSummary(userId: String): DashboardSummary =
        repository.getSummary(userId)

    suspend fun getForecast(userId: String): EnergyForecast =
        repository.getForecast(userId)
}
