package com.smartgrid.domain.repository

import com.smartgrid.domain.model.*

interface WalletRepository {
    suspend fun getBalance(userId: String): WalletInfo
    suspend fun getTransactions(userId: String): List<Transaction>
    suspend fun transfer(userId: String, toAddress: String, amount: Double): Transaction
    suspend fun getRewards(userId: String): List<Reward>
    suspend fun claimReward(userId: String, rewardId: String): Reward
}
