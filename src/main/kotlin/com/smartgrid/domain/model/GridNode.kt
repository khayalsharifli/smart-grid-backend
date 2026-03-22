package com.smartgrid.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class NodeType {
    CONSUMER, PROSUMER, SUBSTATION, TRANSFORMER
}

@Serializable
data class GridNode(
    val id: String,
    val userId: String?,
    val type: NodeType,
    val latitude: Double,
    val longitude: Double,
    val currentLoadKw: Double,
    val maxCapacityKw: Double,
    val isActive: Boolean
)

@Serializable
data class GridHealth(
    val totalNodes: Int,
    val activeNodes: Int,
    val totalLoadKw: Double,
    val maxCapacityKw: Double,
    val healthPercentage: Double,
    val alerts: List<String>
)
