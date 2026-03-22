package com.smartgrid.domain.repository

import com.smartgrid.domain.model.*

interface TradingRepository {
    suspend fun getActiveOffers(): List<EnergyOffer>
    suspend fun getOfferById(id: String): EnergyOffer?
    suspend fun createOffer(offer: EnergyOffer): EnergyOffer
    suspend fun deleteOffer(id: String): Boolean
    suspend fun buyEnergy(offerId: String, buyerId: String): Transaction
    suspend fun getTradeHistory(userId: String): List<Transaction>
    suspend fun getDynamicPrice(): DynamicPrice
}
