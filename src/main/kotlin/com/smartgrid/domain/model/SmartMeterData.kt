package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SmartMeterData(
    val meterId: String,
    val userId: String,
    val consumptionKwh: Double,
    val productionKwh: Double,
    val voltage: Double,
    val frequency: Double,
    val timestamp: Long
)

@Serializable
data class SmartMeter(
    val id: String,
    val userId: String,
    val name: String,
    val model: String,
    val status: MeterStatus,
    val lastReading: Double,
    val installedAt: Long
)

@Serializable
enum class MeterStatus {
    ONLINE, OFFLINE, MAINTENANCE, ERROR
}
