package com.smartgrid.routes

import com.smartgrid.domain.dto.*
import com.smartgrid.domain.service.WalletService
import com.smartgrid.plugins.userId
import com.smartgrid.util.*
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.walletRoutes(walletService: WalletService) {
    authenticate(JwtConstants.AUTH_NAME) {
        route(Routes.WALLET) {
            get(Routes.BALANCE) {
                val principal = call.principal<JWTPrincipal>()!!
                val balance = walletService.getBalance(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = balance))
            }

            get(Routes.TRANSACTIONS) {
                val principal = call.principal<JWTPrincipal>()!!
                val transactions = walletService.getTransactions(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = transactions))
            }

            post(Routes.TRANSFER) {
                val principal = call.principal<JWTPrincipal>()!!
                val request = call.receive<TransferTokenRequest>()
                val transaction = walletService.transfer(principal.userId(), request)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = transaction))
            }

            get(Routes.REWARDS) {
                val principal = call.principal<JWTPrincipal>()!!
                val rewards = walletService.getRewards(principal.userId())
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = rewards))
            }

            post(Routes.CLAIM_REWARD) {
                val principal = call.principal<JWTPrincipal>()!!
                val rewardId = call.queryParameters[Params.REWARD_ID]
                    ?: return@post call.respond(HttpStatusCode.BadRequest, ApiResponse<Nothing>(success = false, error = ErrorMessages.MISSING_REWARD_ID))
                val reward = walletService.claimReward(principal.userId(), rewardId)
                call.respond(HttpStatusCode.OK, ApiResponse(success = true, data = reward))
            }
        }
    }
}
