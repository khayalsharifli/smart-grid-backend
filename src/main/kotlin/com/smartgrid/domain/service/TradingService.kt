package com.smartgrid.domain.service

import com.smartgrid.domain.dto.BuyEnergyRequest
import com.smartgrid.domain.dto.CreateOfferRequest
import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.TradingRepository
import com.smartgrid.util.ErrorMessages
import com.smartgrid.util.IdPrefix
import java.util.UUID

class TradingService(private val repository: TradingRepository) {

    suspend fun getActiveOffers(): List<EnergyOffer> =
        repository.getActiveOffers()

    suspend fun getOfferById(id: String): EnergyOffer =
        repository.getOfferById(id) ?: throw IllegalArgumentException(ErrorMessages.OFFER_NOT_FOUND)

    suspend fun createOffer(sellerId: String, sellerName: String, request: CreateOfferRequest): EnergyOffer {
        val offer = EnergyOffer(
            id = "${IdPrefix.OFFER}${UUID.randomUUID().toString().take(8)}",
            sellerId = sellerId,
            sellerName = sellerName,
            energyAmountKwh = request.energyAmountKwh,
            pricePerKwh = request.pricePerKwh,
            energySource = request.energySource,
            status = OfferStatus.ACTIVE,
            expiresAt = System.currentTimeMillis() + request.durationHours * 3_600_000L,
            createdAt = System.currentTimeMillis()
        )
        return repository.createOffer(offer)
    }

    suspend fun deleteOffer(id: String): Boolean =
        repository.deleteOffer(id)

    suspend fun buyEnergy(buyerId: String, request: BuyEnergyRequest): Transaction =
        repository.buyEnergy(request.offerId, buyerId)

    suspend fun getTradeHistory(userId: String): List<Transaction> =
        repository.getTradeHistory(userId)

    suspend fun getDynamicPrice(): DynamicPrice =
        repository.getDynamicPrice()
}
