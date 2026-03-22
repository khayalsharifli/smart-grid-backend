package com.smartgrid.domain.service

import com.smartgrid.domain.dto.TransferTokenRequest
import com.smartgrid.domain.model.*
import com.smartgrid.domain.repository.WalletRepository

class WalletService(private val repository: WalletRepository) {

    suspend fun getBalance(userId: String): WalletInfo =
        repository.getBalance(userId)

    suspend fun getTransactions(userId: String): List<Transaction> =
        repository.getTransactions(userId)

    suspend fun transfer(userId: String, request: TransferTokenRequest): Transaction =
        repository.transfer(userId, request.toAddress, request.amount)

    suspend fun getRewards(userId: String): List<Reward> =
        repository.getRewards(userId)

    suspend fun claimReward(userId: String, rewardId: String): Reward =
        repository.claimReward(userId, rewardId)
}
