package com.smartgrid

import com.smartgrid.data.mock.*
import com.smartgrid.domain.service.*
import com.smartgrid.plugins.*
import com.smartgrid.util.ServerConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    embeddedServer(Netty, port = ServerConfig.PORT, host = ServerConfig.BIND_HOST, module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val authRepository = MockAuthRepository()
    val energyRepository = MockEnergyRepository()
    val tradingRepository = MockTradingRepository()
    val walletRepository = MockWalletRepository()
    val meterRepository = MockMeterRepository()
    val analyticsRepository = MockAnalyticsRepository()
    val adminRepository = MockAdminRepository(authRepository)
    val landingRepository = MockLandingRepository()

    val authService = AuthService(authRepository)
    val energyService = EnergyService(energyRepository)
    val tradingService = TradingService(tradingRepository)
    val walletService = WalletService(walletRepository)
    val meterService = MeterService(meterRepository)
    val analyticsService = AnalyticsService(analyticsRepository)
    val adminService = AdminService(adminRepository)
    val landingService = LandingService(landingRepository)

    configureSerialization()
    configureSecurity()
    configureStatusPages()
    configureCORS()
    configureWebSockets()

    configureRouting(
        authService = authService,
        energyService = energyService,
        tradingService = tradingService,
        walletService = walletService,
        meterService = meterService,
        analyticsService = analyticsService,
        adminService = adminService,
        landingService = landingService
    )
}
