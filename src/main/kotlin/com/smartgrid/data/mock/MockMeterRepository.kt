package com.smartgrid.data.mock

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.MeterRepository
import com.smartgrid.util.IdPrefix
import com.smartgrid.util.MockData
import com.smartgrid.util.MockIds
import java.util.UUID

class MockMeterRepository : MeterRepository {

    private val meters = mutableListOf(
        SmartMeter(MockIds.METER_001, MockIds.USER_001, MockData.METER_NAME_HOME, MockData.METER_MODEL_ABB, MeterStatus.ONLINE, 2.4, 1710000000000),
        SmartMeter(MockIds.METER_002, MockIds.USER_001, MockData.METER_NAME_GARAGE, MockData.METER_MODEL_SCHNEIDER, MeterStatus.ONLINE, 0.8, 1710100000000),
        SmartMeter(MockIds.METER_003, MockIds.USER_002, MockData.METER_NAME_SOLAR, MockData.METER_MODEL_FRONIUS, MeterStatus.ONLINE, 4.2, 1710200000000),
        SmartMeter(MockIds.METER_004, MockIds.USER_002, MockData.METER_NAME_CONSUMPTION, MockData.METER_MODEL_ABB, MeterStatus.MAINTENANCE, 1.1, 1710300000000)
    )

    override suspend fun getMeters(userId: String): List<SmartMeter> =
        meters.filter { it.userId == userId }

    override suspend fun addMeter(userId: String, name: String, model: String): SmartMeter {
        val meter = SmartMeter(
            id = "${IdPrefix.METER}${UUID.randomUUID().toString().take(8)}",
            userId = userId,
            name = name,
            model = model,
            status = MeterStatus.ONLINE,
            lastReading = 0.0,
            installedAt = System.currentTimeMillis()
        )
        meters.add(meter)
        return meter
    }

    override suspend fun deleteMeter(id: String): Boolean =
        meters.removeIf { it.id == id }

    override suspend fun getMeterData(meterId: String): List<SmartMeterData> {
        val now = System.currentTimeMillis()
        val minuteMs = 60_000L
        val meter = meters.find { it.id == meterId } ?: return emptyList()
        return (0..59).map { i ->
            SmartMeterData(
                meterId = meterId,
                userId = meter.userId,
                consumptionKwh = 0.5 + Math.random() * 2.0,
                productionKwh = if (meter.name.contains(MockData.SOLAR_KEYWORD, ignoreCase = true)) Math.random() * 4.0 else 0.0,
                voltage = 220.0 + Math.random() * 10 - 5,
                frequency = 49.9 + Math.random() * 0.2,
                timestamp = now - (59 - i) * minuteMs
            )
        }
    }
}
