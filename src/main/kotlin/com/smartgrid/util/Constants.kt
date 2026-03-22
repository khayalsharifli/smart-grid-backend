package com.smartgrid.util

object ServerConfig {
    const val BIND_HOST = "0.0.0.0"
    const val DOMAIN = "grid-contract.com"
    const val PORT = 8080
    const val API_VERSION = "/api/v1"
    const val SCHEME_HTTP = "http"
    const val SCHEME_HTTPS = "https"
}

object JwtConstants {
    const val SECRET = "smartgrid-jwt-secret-key-2026"
    const val ISSUER = "smartgrid-backend"
    const val AUDIENCE = "smartgrid-users"
    const val REALM = "SmartGrid Backend"
    const val AUTH_NAME = "auth-jwt"
    const val CLAIM_USER_ID = "userId"
    const val CLAIM_ROLE = "role"
    const val CLAIM_TYPE = "type"
    const val TYPE_REFRESH = "refresh"
    const val ACCESS_TOKEN_VALIDITY_MS = 86_400_000L
    const val REFRESH_TOKEN_VALIDITY_MS = 604_800_000L
    const val EXPIRES_IN_SECONDS = 86400L
}

object Routes {
    const val AUTH = "/auth"
    const val REGISTER = "/register"
    const val LOGIN = "/login"
    const val REFRESH = "/refresh"
    const val WALLET_LINK = "/wallet/link"
    const val PROFILE = "/profile"

    const val ENERGY = "/energy"
    const val CONSUMPTION = "/consumption"
    const val PRODUCTION = "/production"
    const val SUMMARY = "/summary"
    const val FORECAST = "/forecast"

    const val TRADING = "/trading"
    const val OFFERS = "/offers"
    const val OFFER_BY_ID = "/offers/{id}"
    const val BUY = "/buy"
    const val HISTORY = "/history"
    const val PRICE = "/price"

    const val WALLET = "/wallet"
    const val BALANCE = "/balance"
    const val TRANSACTIONS = "/transactions"
    const val TRANSFER = "/transfer"
    const val REWARDS = "/rewards"
    const val CLAIM_REWARD = "/claim-reward"

    const val METERS = "/meters"
    const val METER_BY_ID = "/{id}"
    const val METER_DATA = "/{id}/data"
    const val METER_STREAM = "/api/v1/meters/stream"

    const val ANALYTICS = "/analytics"
    const val CARBON = "/carbon"
    const val COMPARISON = "/comparison"

    const val LANDING = "/landing"
    const val HERO = "/hero"
    const val FEATURES = "/features"
    const val HOW_IT_WORKS = "/how-it-works"
    const val PLATFORM_STATS = "/stats"
    const val CTA = "/cta"
    const val FOOTER = "/footer"

    const val ADMIN = "/admin"
    const val USERS = "/users"
    const val USER_ROLE = "/users/{id}/role"
    const val USER_BLOCK = "/users/{id}/block"
    const val GRID_HEALTH = "/grid/health"
    const val GRID_EMERGENCY = "/grid/emergency"
    const val CONTRACTS = "/contracts"
    const val CONTRACTS_DEPLOY = "/contracts/deploy"
}

object Params {
    const val ID = "id"
    const val PERIOD = "period"
    const val PERIOD1 = "period1"
    const val PERIOD2 = "period2"
    const val REWARD_ID = "rewardId"
}

object Defaults {
    const val PERIOD_DAILY = "daily"
    const val PERIOD_WEEKLY = "weekly"
    const val PERIOD_MONTHLY = "monthly"
    const val DEFAULT_PERIOD1 = "2026-02"
    const val DEFAULT_PERIOD2 = "2026-03"
    const val FORECAST_PERIOD_24H = "24h"
}

object ErrorMessages {
    const val EMAIL_ALREADY_REGISTERED = "Email already registered"
    const val INVALID_CREDENTIALS = "Invalid email or password"
    const val INVALID_REFRESH_TOKEN = "Invalid refresh token"
    const val USER_NOT_FOUND = "User not found"
    const val OFFER_NOT_FOUND = "Offer not found"
    const val REWARD_NOT_FOUND = "Reward not found"
    const val MISSING_ID = "Missing ID"
    const val MISSING_REWARD_ID = "Missing rewardId"
    const val INTERNAL_SERVER_ERROR = "Internal server error"
    const val RESOURCE_NOT_FOUND = "Resource not found"
    const val TOKEN_INVALID_OR_EXPIRED = "Token is not valid or has expired"
    const val ADMIN_ACCESS_REQUIRED = "Admin access required"
    const val ADMIN_OR_OPERATOR_REQUIRED = "Admin or Grid Operator access required"
}

object SuccessMessages {
    const val REGISTRATION_SUCCESSFUL = "Registration successful"
    const val OFFER_CANCELLED = "Offer cancelled"
    const val METER_DELETED = "Meter deleted"
    const val EMERGENCY_SHUTDOWN_INITIATED = "Emergency shutdown initiated"
}

object IdPrefix {
    const val USER = "user-"
    const val OFFER = "offer-"
    const val TRANSACTION = "tx-"
    const val METER = "meter-"
    const val HEX = "0x"
}

object Network {
    const val SEPOLIA = "sepolia"
}

object MockIds {
    const val USER_001 = "user-001"
    const val USER_002 = "user-002"
    const val USER_003 = "user-003"
    const val USER_004 = "user-004"
    const val OFFER_001 = "offer-001"
    const val OFFER_002 = "offer-002"
    const val OFFER_003 = "offer-003"
    const val OFFER_004 = "offer-004"
    const val TX_001 = "tx-001"
    const val TX_002 = "tx-002"
    const val METER_001 = "meter-001"
    const val METER_002 = "meter-002"
    const val METER_003 = "meter-003"
    const val METER_004 = "meter-004"
    const val REWARD_001 = "reward-001"
    const val REWARD_002 = "reward-002"
    const val REWARD_003 = "reward-003"
}

object MockData {
    const val CONSUMER_EMAIL = "consumer@smartgrid.az"
    const val PROSUMER_EMAIL = "prosumer@smartgrid.az"
    const val ADMIN_EMAIL = "admin@smartgrid.az"
    const val OPERATOR_EMAIL = "operator@smartgrid.az"
    const val CONSUMER_NAME = "Elvin Mammadov"
    const val PROSUMER_NAME = "Aysel Huseynova"
    const val ADMIN_NAME = "Khayal Sharifli"
    const val OPERATOR_NAME = "Nigar Aliyeva"
    const val CONSUMER_WALLET = "0x1234abcd5678ef90"
    const val PROSUMER_WALLET = "0xabcd1234ef567890"
    const val ADMIN_WALLET = "0xef901234abcd5678"
    const val OPERATOR_WALLET = "0x5678ef90abcd1234"
    const val CONSUMER_PASSWORD_HASH = "hashed_password123"
    const val PROSUMER_PASSWORD_HASH = "hashed_password456"
    const val ADMIN_PASSWORD_HASH = "hashed_password789"
    const val OPERATOR_PASSWORD_HASH = "hashed_password000"
    const val MOCK_TX_HASH_1 = "0xabc123def456789012345678901234567890abcd"
    const val MOCK_TX_HASH_2 = "0xdef456789012345678901234567890abcdabc123"
    const val MOCK_WALLET_FULL = "0x1234abcd5678ef901234abcd5678ef90"
    const val MOCK_TX_HASH_W1 = "0xaaa111bbb222ccc333ddd444eee555fff666"
    const val MOCK_TX_HASH_W2 = "0xbbb222ccc333ddd444eee555fff666aaa111"
    const val MOCK_TX_HASH_W3 = "0xccc333ddd444eee555fff666aaa111bbb222"
    const val CONTRACT_ENERGY_TOKEN = "EnergyToken"
    const val CONTRACT_ENERGY_TRADING = "EnergyTrading"
    const val CONTRACT_METER_REGISTRY = "SmartMeterRegistry"
    const val CONTRACT_ADDR_1 = "0xContractAddr001"
    const val CONTRACT_ADDR_2 = "0xContractAddr002"
    const val CONTRACT_ADDR_3 = "0xContractAddr003"
    const val METER_NAME_HOME = "Ev sayqaci"
    const val METER_NAME_GARAGE = "Qaraj sayqaci"
    const val METER_NAME_SOLAR = "Solar panel sayqaci"
    const val METER_NAME_CONSUMPTION = "Ev istehlak sayqaci"
    const val METER_MODEL_ABB = "ABB A44"
    const val METER_MODEL_SCHNEIDER = "Schneider iEM3155"
    const val METER_MODEL_FRONIUS = "Fronius Smart Meter"
    const val REWARD_REASON_1 = "Enerji qenayeti bonusu - Yanvar"
    const val REWARD_REASON_2 = "Gunduz saatlarinda az istehlak"
    const val REWARD_REASON_3 = "Solar enerji istehsali mukafati"
    const val ALERT_HIGH_CONSUMPTION = "Node #12 yuksek istehlak - 4.8kW"
    const val ALERT_CONNECTION_DELAY = "Node #37 baglanti kecikmes - 250ms"
    const val CARBON_MONTH_1 = "2026-01"
    const val CARBON_MONTH_2 = "2026-02"
    const val CARBON_MONTH_3 = "2026-03"
    const val HASH_PREFIX = "hashed_"
    const val SOLAR_KEYWORD = "Solar"
}
