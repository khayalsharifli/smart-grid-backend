package com.smartgrid.data.mock

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.TradingRepository
import com.smartgrid.util.ErrorMessages
import com.smartgrid.util.IdPrefix
import com.smartgrid.util.MockData
import com.smartgrid.util.MockIds
import java.util.UUID

class MockTradingRepository : TradingRepository {

    private val offers = mutableListOf(
        EnergyOffer(MockIds.OFFER_001, MockIds.USER_002, MockData.PROSUMER_NAME, 15.0, 0.25, EnergySource.SOLAR, OfferStatus.ACTIVE, System.currentTimeMillis() + 86400000, System.currentTimeMillis() - 3600000),
        EnergyOffer(MockIds.OFFER_002, MockIds.USER_002, MockData.PROSUMER_NAME, 8.0, 0.20, EnergySource.WIND, OfferStatus.ACTIVE, System.currentTimeMillis() + 172800000, System.currentTimeMillis() - 7200000),
        EnergyOffer(MockIds.OFFER_003, MockIds.USER_002, MockData.PROSUMER_NAME, 25.0, 0.30, EnergySource.SOLAR, OfferStatus.ACTIVE, System.currentTimeMillis() + 43200000, System.currentTimeMillis() - 1800000),
        EnergyOffer(MockIds.OFFER_004, MockIds.USER_002, MockData.PROSUMER_NAME, 5.0, 0.18, EnergySource.HYDRO, OfferStatus.SOLD, System.currentTimeMillis() - 3600000, System.currentTimeMillis() - 86400000)
    )

    private val transactions = mutableListOf(
        Transaction(MockIds.TX_001, MockData.MOCK_TX_HASH_1, MockIds.USER_001, MockIds.USER_002, MockIds.OFFER_004, 5.0, 0.90, TransactionType.ENERGY_PURCHASE, TransactionStatus.SETTLED, System.currentTimeMillis() - 86400000),
        Transaction(MockIds.TX_002, MockData.MOCK_TX_HASH_2, MockIds.USER_001, MockIds.USER_002, MockIds.OFFER_002, 3.0, 0.60, TransactionType.ENERGY_PURCHASE, TransactionStatus.CONFIRMED, System.currentTimeMillis() - 43200000)
    )

    override suspend fun getActiveOffers(): List<EnergyOffer> =
        offers.filter { it.status == OfferStatus.ACTIVE }

    override suspend fun getOfferById(id: String): EnergyOffer? =
        offers.find { it.id == id }

    override suspend fun createOffer(offer: EnergyOffer): EnergyOffer {
        offers.add(offer)
        return offer
    }

    override suspend fun deleteOffer(id: String): Boolean {
        val index = offers.indexOfFirst { it.id == id }
        if (index == -1) return false
        offers[index] = offers[index].copy(status = OfferStatus.CANCELLED)
        return true
    }

    override suspend fun buyEnergy(offerId: String, buyerId: String): Transaction {
        val offer = offers.find { it.id == offerId } ?: throw IllegalArgumentException(ErrorMessages.OFFER_NOT_FOUND)
        val offerIndex = offers.indexOf(offer)
        offers[offerIndex] = offer.copy(status = OfferStatus.SOLD)

        val tx = Transaction(
            id = "${IdPrefix.TRANSACTION}${UUID.randomUUID().toString().take(8)}",
            txHash = "${IdPrefix.HEX}${UUID.randomUUID().toString().replace("-", "")}",
            buyerId = buyerId,
            sellerId = offer.sellerId,
            offerId = offerId,
            energyAmountKwh = offer.energyAmountKwh,
            totalPrice = offer.energyAmountKwh * offer.pricePerKwh,
            type = TransactionType.ENERGY_PURCHASE,
            status = TransactionStatus.PENDING,
            createdAt = System.currentTimeMillis()
        )
        transactions.add(tx)
        return tx
    }

    override suspend fun getTradeHistory(userId: String): List<Transaction> =
        transactions.filter { it.buyerId == userId || it.sellerId == userId }

    override suspend fun getDynamicPrice(): DynamicPrice = DynamicPrice(
        currentPricePerKwh = 0.24,
        avgPricePerKwh = 0.22,
        supplyKwh = 1250.0,
        demandKwh = 980.0,
        trend = PriceTrend.STABLE,
        updatedAt = System.currentTimeMillis()
    )
}
