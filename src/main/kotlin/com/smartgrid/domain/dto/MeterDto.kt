package com.smartgrid.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class AddMeterRequest(
    val name: String,
    val model: String
)
