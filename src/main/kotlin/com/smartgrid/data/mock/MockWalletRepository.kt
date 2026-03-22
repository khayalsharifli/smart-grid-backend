package com.smartgrid.data.mock

import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.WalletRepository
import com.smartgrid.util.ErrorMessages
import com.smartgrid.util.IdPrefix
import com.smartgrid.util.MockData
import com.smartgrid.util.MockIds
import java.util.UUID

class MockWalletRepository : WalletRepository {

    private val rewards = mutableListOf(
        Reward(MockIds.REWARD_001, MockIds.USER_001, 10.0, MockData.REWARD_REASON_1, false, System.currentTimeMillis() - 2592000000),
        Reward(MockIds.REWARD_002, MockIds.USER_001, 5.0, MockData.REWARD_REASON_2, true, System.currentTimeMillis() - 1296000000),
        Reward(MockIds.REWARD_003, MockIds.USER_002, 20.0, MockData.REWARD_REASON_3, false, System.currentTimeMillis() - 604800000)
    )

    override suspend fun getBalance(userId: String): WalletInfo = WalletInfo(
        userId = userId,
        walletAddress = MockData.MOCK_WALLET_FULL,
        ethBalance = 0.15,
        energyTokenBalance = 245.0,
        fiatEquivalentAzn = 416.50
    )

    override suspend fun getTransactions(userId: String): List<Transaction> = listOf(
        Transaction("tx-w01", MockData.MOCK_TX_HASH_W1, userId, MockIds.USER_002, MockIds.OFFER_001, 10.0, 2.50, TransactionType.ENERGY_PURCHASE, TransactionStatus.SETTLED, System.currentTimeMillis() - 86400000),
        Transaction("tx-w02", MockData.MOCK_TX_HASH_W2, userId, MockIds.USER_002, MockIds.OFFER_002, 5.0, 1.00, TransactionType.TOKEN_TRANSFER, TransactionStatus.CONFIRMED, System.currentTimeMillis() - 172800000),
        Transaction("tx-w03", MockData.MOCK_TX_HASH_W3, userId, "", "", 0.0, 15.0, TransactionType.REWARD_CLAIM, TransactionStatus.SETTLED, System.currentTimeMillis() - 259200000)
    )

    override suspend fun transfer(userId: String, toAddress: String, amount: Double): Transaction = Transaction(
        id = "${IdPrefix.TRANSACTION}${UUID.randomUUID().toString().take(8)}",
        txHash = "${IdPrefix.HEX}${UUID.randomUUID().toString().replace("-", "")}",
        buyerId = userId,
        sellerId = "",
        offerId = "",
        energyAmountKwh = 0.0,
        totalPrice = amount,
        type = TransactionType.TOKEN_TRANSFER,
        status = TransactionStatus.PENDING,
        createdAt = System.currentTimeMillis()
    )

    override suspend fun getRewards(userId: String): List<Reward> =
        rewards.filter { it.userId == userId }

    override suspend fun claimReward(userId: String, rewardId: String): Reward {
        val index = rewards.indexOfFirst { it.id == rewardId && it.userId == userId }
        if (index == -1) throw IllegalArgumentException(ErrorMessages.REWARD_NOT_FOUND)
        val claimed = rewards[index].copy(claimed = true)
        rewards[index] = claimed
        return claimed
    }
}
