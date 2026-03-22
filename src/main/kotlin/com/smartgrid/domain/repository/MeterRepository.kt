package com.smartgrid.domain.repository

import com.smartgrid.domain.model.*

interface MeterRepository {
    suspend fun getMeters(userId: String): List<SmartMeter>
    suspend fun addMeter(userId: String, name: String, model: String): SmartMeter
    suspend fun deleteMeter(id: String): Boolean
    suspend fun getMeterData(meterId: String): List<SmartMeterData>
}
