package com.smartgrid.domain.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransferTokenRequest(
    val toAddress: String,
    val amount: Double
)
