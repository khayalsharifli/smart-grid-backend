package com.smartgrid.plugins

import com.smartgrid.domain.service.*
import com.smartgrid.domain.service.LandingService
import com.smartgrid.routes.*
import com.smartgrid.util.ServerConfig
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    authService: AuthService,
    energyService: EnergyService,
    tradingService: TradingService,
    walletService: WalletService,
    meterService: MeterService,
    analyticsService: AnalyticsService,
    adminService: AdminService,
    landingService: LandingService
) {
    routing {
        route(ServerConfig.API_VERSION) {
            landingRoutes(landingService)
            authRoutes(authService)
            energyRoutes(energyService)
            tradingRoutes(tradingService)
            walletRoutes(walletService)
            meterRoutes(meterService)
            analyticsRoutes(analyticsService)
            adminRoutes(adminService)
        }
    }
}
