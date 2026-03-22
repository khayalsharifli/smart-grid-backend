package com.smartgrid.domain.service

import com.smartgrid.domain.dto.AddMeterRequest
import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.MeterRepository

class MeterService(private val repository: MeterRepository) {

    suspend fun getMeters(userId: String): List<SmartMeter> =
        repository.getMeters(userId)

    suspend fun addMeter(userId: String, request: AddMeterRequest): SmartMeter =
        repository.addMeter(userId, request.name, request.model)

    suspend fun deleteMeter(id: String): Boolean =
        repository.deleteMeter(id)

    suspend fun getMeterData(meterId: String): List<SmartMeterData> =
        repository.getMeterData(meterId)
}
