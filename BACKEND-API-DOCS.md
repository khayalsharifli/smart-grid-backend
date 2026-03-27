# SmartGrid Backend API Documentation

**Base URL:** `http://localhost:8080/api/v1`
**Framework:** Ktor (Kotlin)
**Authentication:** JWT Bearer Token (HS256)
**Content-Type:** `application/json`

---

## Table of Contents

1. [Landing Page API](#1-landing-page-api-public)
2. [Authentication API](#2-authentication-api)
3. [Energy API](#3-energy-api-)
4. [Trading API](#4-trading-api-)
5. [Wallet API](#5-wallet-api-)
6. [Meter API](#6-meter-api-)
7. [Analytics API](#7-analytics-api-)
8. [Admin API](#8-admin-api--admin-role-required)
9. [Error Handling](#9-error-handling)
10. [Test Credentials](#10-mock-test-credentials)

---

## Response Envelope

All API responses follow a standard wrapper format:

```json
{
  "success": true,
  "data": { ... },
  "message": "Optional success message",
  "error": null
}
```

On error:
```json
{
  "success": false,
  "data": null,
  "error": "Error description"
}
```

---

## 1. Landing Page API (Public)

These endpoints return landing page content. No authentication required.

### GET /landing

Returns the complete landing page data in a single request.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "navigation": [
      { "label": "About", "url": "#about" },
      { "label": "Features", "url": "#features" },
      { "label": "How It Works", "url": "#how-it-works" },
      { "label": "Stats", "url": "#stats" }
    ],
    "hero": { ... },
    "features": { ... },
    "howItWorks": { ... },
    "stats": { ... },
    "cta": { ... },
    "footer": { ... }
  }
}
```

---

### GET /landing/hero

Returns hero section data with platform statistics highlights.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "title": "The Future of Energy is Decentralized",
    "subtitle": "Join the clean energy revolution. Buy, sell, and trade renewable energy directly with your community — no middleman, full transparency.",
    "primaryButtonText": "Get Started",
    "secondaryButtonText": "Watch Demo",
    "stats": [
      { "value": "2,400+", "label": "Active Prosumers" },
      { "value": "15 GWh", "label": "Energy Traded" },
      { "value": "98.6%", "label": "Uptime" }
    ]
  }
}
```

---

### GET /landing/features

Returns the platform features list ("Why SmartGrid?").

**Response (200):**
```json
{
  "success": true,
  "data": {
    "badge": "Features",
    "title": "Why SmartGrid?",
    "subtitle": "Built for the next generation of energy infrastructure. Trade smarter, live greener.",
    "items": [
      {
        "id": "feature-p2p",
        "icon": "arrows-exchange",
        "title": "Peer-to-Peer Trading",
        "description": "Trade energy directly with your neighbors..."
      },
      {
        "id": "feature-contracts",
        "icon": "file-contract",
        "title": "Smart Contracts",
        "description": "Automated and transparent energy transactions..."
      },
      {
        "id": "feature-analytics",
        "icon": "chart-line",
        "title": "Real-time Analytics",
        "description": "Monitor your energy consumption and production..."
      }
    ]
  }
}
```

---

### GET /landing/how-it-works

Returns the onboarding steps section.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "badge": "How It Works",
    "title": "Start Trading in Minutes",
    "subtitle": "Three simple steps to join the decentralized energy revolution.",
    "steps": [
      {
        "step": 1,
        "icon": "wallet",
        "title": "Create Your Wallet",
        "description": "Sign up and link your blockchain wallet to get started..."
      },
      {
        "step": 2,
        "icon": "bolt",
        "title": "List Your Energy",
        "description": "If you produce renewable energy via solar panels..."
      },
      {
        "step": 3,
        "icon": "exchange",
        "title": "Earn & Trade",
        "description": "Buy renewable energy from local producers..."
      }
    ]
  }
}
```

---

### GET /landing/stats

Returns platform-wide statistics.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "badge": "Platform Statistics",
    "title": "Trusted by Energy Pioneers Worldwide",
    "items": [
      { "value": "2,400+", "label": "Active Prosumers" },
      { "value": "15 GWh", "label": "Energy Traded" },
      { "value": "$2.1M", "label": "Token Savings" },
      { "value": "98.6%", "label": "Return Rate" }
    ]
  }
}
```

---

### GET /landing/cta

Returns the call-to-action section.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "title": "Ready to Power the Future?",
    "subtitle": "Join thousands of energy pioneers trading renewable power on the blockchain. Free to join. Easy to use.",
    "primaryButtonText": "Join SmartGrid",
    "secondaryButtonText": "Watch Demo"
  }
}
```

---

### GET /landing/footer

Returns footer data including navigation links, social links, and copyright.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "companyName": "SmartGrid",
    "companyDescription": "The next generation energy trading platform...",
    "columns": [
      {
        "title": "Products",
        "links": [
          { "label": "Marketplace", "url": "/marketplace" },
          { "label": "Analytics", "url": "/analytics" },
          { "label": "Smart Meters", "url": "/meters" },
          { "label": "Wallet", "url": "/wallet" }
        ]
      },
      {
        "title": "Company",
        "links": [...]
      },
      {
        "title": "Resources",
        "links": [...]
      }
    ],
    "copyright": "© 2026 SmartGrid Platform. All rights reserved.",
    "socialLinks": [
      { "platform": "twitter", "url": "https://twitter.com/smartgrid" },
      { "platform": "github", "url": "https://github.com/smartgrid" },
      { "platform": "linkedin", "url": "https://linkedin.com/company/smartgrid" },
      { "platform": "discord", "url": "https://discord.gg/smartgrid" }
    ]
  }
}
```

---

## 2. Authentication API

### POST /auth/register

Register a new user account.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "name": "John Doe",
  "role": "CONSUMER"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `email` | string | Yes | Valid email address |
| `password` | string | Yes | User password |
| `name` | string | Yes | Full name |
| `role` | enum | Yes | One of: `CONSUMER`, `PROSUMER`, `GRID_OPERATOR`, `ADMIN` |

**Response (201):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 86400,
    "userId": "user-a1b2c3d4"
  },
  "message": "Registration successful"
}
```

**Errors:**
- `400` — "Email already registered" if the email is already in use

---

### POST /auth/login

Authenticate an existing user and receive JWT tokens.

**Request Body:**
```json
{
  "email": "consumer@smartgrid.az",
  "password": "password123"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `email` | string | Yes | Registered email address |
| `password` | string | Yes | Account password |

**Response (200):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
    "expiresIn": 86400,
    "userId": "user-001"
  }
}
```

**Errors:**
- `400` — "Invalid email or password"

---

### POST /auth/refresh

Refresh an expired access token using a valid refresh token.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response (200):**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIs...(new)",
    "refreshToken": "eyJhbGciOiJIUzI1NiIs...(new)",
    "expiresIn": 86400,
    "userId": "user-001"
  }
}
```

**Errors:**
- `400` — "Invalid refresh token"

---

### POST /auth/wallet/link 🔒

Link an Ethereum wallet address to the authenticated user's account.

**Headers:** `Authorization: Bearer <access_token>`

**Request Body:**
```json
{
  "walletAddress": "0x1234abcd5678ef901234abcd5678ef90",
  "signature": "0xSignedMessage..."
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `walletAddress` | string | Yes | Ethereum wallet address |
| `signature` | string | Yes | Signed message proving wallet ownership |

**Response (200):**
```json
{
  "success": true,
  "data": {
    "id": "user-001",
    "email": "consumer@smartgrid.az",
    "name": "John Doe",
    "role": "CONSUMER",
    "walletAddress": "0x1234abcd5678ef901234abcd5678ef90",
    "createdAt": 1710000000000
  }
}
```

---

### GET /auth/profile 🔒

Retrieve the authenticated user's profile information.

**Headers:** `Authorization: Bearer <access_token>`

**Response (200):**
```json
{
  "success": true,
  "data": {
    "id": "user-001",
    "email": "consumer@smartgrid.az",
    "name": "John Doe",
    "role": "CONSUMER",
    "walletAddress": "0x1234abcd5678ef90",
    "createdAt": 1710000000000
  }
}
```

---

### PUT /auth/profile 🔒

Update the authenticated user's profile (name and/or email).

**Headers:** `Authorization: Bearer <access_token>`

**Request Body:**
```json
{
  "name": "John D.",
  "email": "john.new@smartgrid.az"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | string | No | Updated display name |
| `email` | string | No | Updated email address |

**Response (200):** Updated User object.

---

## 3. Energy API 🔒

All energy endpoints require a valid JWT token in the `Authorization` header.

### GET /energy/consumption

Retrieve historical energy consumption data for the authenticated user.

**Query Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `period` | enum | `daily` | One of: `daily`, `weekly`, `monthly` |

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "timestamp": 1711100000000,
      "consumptionKwh": 2.35,
      "productionKwh": 0.0
    },
    {
      "timestamp": 1711103600000,
      "consumptionKwh": 1.87,
      "productionKwh": 0.0
    }
  ]
}
```

---

### GET /energy/production

Retrieve historical energy production data. Primarily relevant for prosumers.

**Query Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `period` | enum | `daily` | One of: `daily`, `weekly`, `monthly` |

**Response (200):** Array of `EnergyDataPoint` objects with `timestamp`, `consumptionKwh`, and `productionKwh`.

---

### GET /energy/summary

Retrieve the full dashboard summary including current readings, daily totals, wallet balance, and recent activity.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "userId": "user-001",
    "role": "CONSUMER",
    "currentConsumptionKw": 2.4,
    "currentProductionKw": 0.0,
    "todayConsumptionKwh": 18.7,
    "todayProductionKwh": 0.0,
    "ethBalance": 0.15,
    "energyTokenBalance": 245.0,
    "recentTransactions": [...],
    "activeOffers": 3
  }
}
```

---

### GET /energy/forecast

Retrieve a 24-hour energy consumption and production forecast generated by ML models.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "userId": "user-001",
    "forecastPeriod": "24h",
    "predictedConsumptionKwh": 22.5,
    "predictedProductionKwh": 15.0,
    "confidence": 0.87,
    "dataPoints": [
      {
        "timestamp": 1711200000000,
        "predictedKwh": 2.1,
        "lowerBound": 1.68,
        "upperBound": 2.52
      }
    ]
  }
}
```

---

## 4. Trading API 🔒

All trading endpoints require a valid JWT token.

### GET /trading/offers

List all currently active energy offers on the marketplace.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "offer-001",
      "sellerId": "user-002",
      "sellerName": "Aysel Huseynova",
      "energyAmountKwh": 15.0,
      "pricePerKwh": 0.25,
      "energySource": "SOLAR",
      "status": "ACTIVE",
      "expiresAt": 1711200000000,
      "createdAt": 1711100000000
    }
  ]
}
```

---

### GET /trading/offers/{id}

Retrieve details of a specific energy offer by its ID.

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Offer ID (e.g., `offer-001`) |

**Response (200):** Single `EnergyOffer` object.

**Errors:**
- `404` — "Offer not found"

---

### POST /trading/offers

Create a new energy offer on the marketplace. Only prosumers typically create offers.

**Request Body:**
```json
{
  "energyAmountKwh": 10.0,
  "pricePerKwh": 0.22,
  "energySource": "SOLAR",
  "durationHours": 24
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `energyAmountKwh` | double | Yes | Amount of energy to sell (kWh) |
| `pricePerKwh` | double | Yes | Price per kWh in tokens |
| `energySource` | enum | Yes | One of: `SOLAR`, `WIND`, `HYDRO`, `MIXED` |
| `durationHours` | int | Yes | Offer validity period in hours |

**Response (201):** Created `EnergyOffer` object.

---

### DELETE /trading/offers/{id}

Cancel an active energy offer. Only the offer creator can cancel it.

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Offer ID to cancel |

**Response (200):**
```json
{
  "success": true,
  "message": "Offer cancelled"
}
```

**Errors:**
- `404` — "Offer not found"

---

### POST /trading/buy

Purchase energy from an active offer. Triggers a blockchain smart contract transaction.

**Request Body:**
```json
{
  "offerId": "offer-001",
  "buyerWalletAddress": "0x1234abcd5678ef90"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `offerId` | string | Yes | ID of the offer to purchase |
| `buyerWalletAddress` | string | Yes | Buyer's Ethereum wallet address |

**Response (200):** `Transaction` object with `status: "PENDING"`.

---

### GET /trading/history

Retrieve the authenticated user's complete trade history.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "tx-001",
      "txHash": "0xabc123...",
      "buyerId": "user-001",
      "sellerId": "user-002",
      "offerId": "offer-001",
      "energyAmountKwh": 15.0,
      "totalPrice": 3.75,
      "type": "ENERGY_PURCHASE",
      "status": "CONFIRMED",
      "createdAt": 1711100000000
    }
  ]
}
```

**Transaction Types:** `ENERGY_PURCHASE`, `TOKEN_TRANSFER`, `REWARD_CLAIM`
**Transaction Statuses:** `PENDING`, `CONFIRMED`, `DELIVERED`, `SETTLED`, `FAILED`

---

### GET /trading/price

Retrieve the current dynamic market price based on supply and demand.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "currentPricePerKwh": 0.24,
    "avgPricePerKwh": 0.22,
    "supplyKwh": 1250.0,
    "demandKwh": 980.0,
    "trend": "STABLE",
    "updatedAt": 1711100000000
  }
}
```

**Trend Values:** `RISING`, `FALLING`, `STABLE`

---

## 5. Wallet API 🔒

All wallet endpoints require a valid JWT token.

### GET /wallet/balance

Retrieve the authenticated user's wallet balance including ETH, energy tokens, and fiat equivalent.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "userId": "user-001",
    "walletAddress": "0x1234abcd5678ef90...",
    "ethBalance": 0.15,
    "energyTokenBalance": 245.0,
    "fiatEquivalentAzn": 416.50
  }
}
```

---

### GET /wallet/transactions

Retrieve the authenticated user's wallet transaction history.

**Response (200):** Array of `Transaction` objects.

---

### POST /wallet/transfer

Transfer energy tokens to another wallet address.

**Request Body:**
```json
{
  "toAddress": "0xabcd1234ef567890...",
  "amount": 50.0
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `toAddress` | string | Yes | Recipient's Ethereum wallet address |
| `amount` | double | Yes | Amount of energy tokens to transfer |

**Response (200):** `Transaction` object with `status: "PENDING"`.

---

### GET /wallet/rewards

Retrieve the authenticated user's reward history.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "reward-001",
      "userId": "user-001",
      "amount": 10.0,
      "reason": "Energy savings bonus - January",
      "claimed": false,
      "createdAt": 1708500000000
    },
    {
      "id": "reward-002",
      "userId": "user-001",
      "amount": 25.0,
      "reason": "7-day consecutive savings streak",
      "claimed": true,
      "createdAt": 1709200000000
    }
  ]
}
```

---

### POST /wallet/claim-reward

Claim a pending reward and receive energy tokens.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `rewardId` | string | Yes | ID of the reward to claim (e.g., `reward-001`) |

**Response (200):** `Reward` object with `claimed: true`.

**Errors:**
- `404` — "Reward not found"

---

## 6. Meter API 🔒

All meter endpoints require a valid JWT token.

### GET /meters

List all smart meters registered to the authenticated user.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "meter-001",
      "userId": "user-001",
      "name": "Home Meter",
      "model": "ABB A44",
      "status": "ONLINE",
      "lastReading": 2.4,
      "installedAt": 1710000000000
    }
  ]
}
```

**Meter Statuses:** `ONLINE`, `OFFLINE`, `MAINTENANCE`, `ERROR`

---

### POST /meters

Register a new smart meter device.

**Request Body:**
```json
{
  "name": "Home Meter",
  "model": "ABB A44"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `name` | string | Yes | Display name for the meter |
| `model` | string | Yes | Hardware model identifier |

**Response (201):** Created `SmartMeter` object.

---

### DELETE /meters/{id}

Remove a smart meter from the user's account.

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Meter ID (e.g., `meter-001`) |

**Response (200):**
```json
{
  "success": true,
  "message": "Meter deleted"
}
```

---

### GET /meters/{id}/data

Retrieve recent readings from a specific smart meter (last 60 minutes of data).

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Meter ID |

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "meterId": "meter-001",
      "userId": "user-001",
      "consumptionKwh": 1.85,
      "productionKwh": 0.0,
      "voltage": 221.3,
      "frequency": 50.02,
      "timestamp": 1711100000000
    }
  ]
}
```

---

### WebSocket: /api/v1/meters/stream

Establish a WebSocket connection for real-time smart meter data streaming. The server pushes `SmartMeterData` objects every 2 seconds.

**Connection:** `ws://localhost:8080/api/v1/meters/stream`

**Message Format (server → client):**
```json
{
  "meterId": "meter-001",
  "userId": "user-001",
  "consumptionKwh": 1.85,
  "productionKwh": 0.0,
  "voltage": 221.3,
  "frequency": 50.02,
  "timestamp": 1711100000000
}
```

---

## 7. Analytics API 🔒

All analytics endpoints require a valid JWT token.

### GET /analytics/energy

Retrieve energy analytics and trend data for the authenticated user.

**Query Parameters:**

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `period` | enum | `daily` | One of: `daily`, `weekly`, `monthly` |

**Response (200):**
```json
{
  "success": true,
  "data": {
    "userId": "user-001",
    "period": "monthly",
    "totalConsumptionKwh": 555.0,
    "totalProductionKwh": 369.0,
    "avgDailyConsumption": 18.5,
    "avgDailyProduction": 12.3,
    "peakConsumptionKwh": 4.8,
    "dataPoints": [...]
  }
}
```

---

### GET /analytics/carbon

Retrieve the authenticated user's carbon footprint and environmental impact data.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "userId": "user-001",
    "totalCarbonSavedKg": 156.8,
    "treesEquivalent": 7,
    "greenEnergyPercentage": 64.5,
    "monthlyData": [
      { "month": "2026-01", "carbonSavedKg": 48.2 },
      { "month": "2026-02", "carbonSavedKg": 52.1 },
      { "month": "2026-03", "carbonSavedKg": 56.5 }
    ]
  }
}
```

---

### GET /analytics/comparison

Compare energy analytics between two time periods.

**Query Parameters:**

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| `period1` | string | Yes | First period (e.g., `2026-02`) |
| `period2` | string | Yes | Second period (e.g., `2026-03`) |

**Response (200):**
```json
{
  "success": true,
  "data": {
    "2026-02": {
      "userId": "user-001",
      "period": "2026-02",
      "totalConsumptionKwh": 520.0,
      "totalProductionKwh": 340.0,
      "avgDailyConsumption": 18.6,
      "avgDailyProduction": 12.1,
      "peakConsumptionKwh": 4.5,
      "dataPoints": [...]
    },
    "2026-03": {
      "userId": "user-001",
      "period": "2026-03",
      "totalConsumptionKwh": 555.0,
      "totalProductionKwh": 369.0,
      "avgDailyConsumption": 18.5,
      "avgDailyProduction": 12.3,
      "peakConsumptionKwh": 4.8,
      "dataPoints": [...]
    }
  }
}
```

---

## 8. Admin API 🔒 (ADMIN Role Required)

All admin endpoints require a valid JWT token with `ADMIN` role (unless otherwise noted).

### GET /admin/users

List all registered users on the platform.

**Response (200):** Array of `User` objects.

---

### PUT /admin/users/{id}/role

Change a user's role.

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Target user ID |

**Request Body:**
```json
{
  "role": "PROSUMER"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `role` | enum | Yes | One of: `CONSUMER`, `PROSUMER`, `GRID_OPERATOR`, `ADMIN` |

**Response (200):** Updated `User` object.

---

### PUT /admin/users/{id}/block

Block or unblock a user account.

**Path Parameters:**

| Parameter | Type | Description |
|-----------|------|-------------|
| `id` | string | Target user ID |

**Request Body:**
```json
{
  "blocked": true,
  "reason": "Suspicious activity detected"
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `blocked` | boolean | Yes | `true` to block, `false` to unblock |
| `reason` | string | No | Reason for blocking |

**Response (200):** Updated `User` object.

---

### GET /admin/grid/health

Retrieve current grid health status and alerts. Accessible by `ADMIN` and `GRID_OPERATOR` roles.

**Response (200):**
```json
{
  "success": true,
  "data": {
    "totalNodes": 48,
    "activeNodes": 45,
    "totalLoadKw": 320.5,
    "maxCapacityKw": 500.0,
    "healthPercentage": 93.75,
    "alerts": [
      "Node #12 high consumption - 4.8kW",
      "Node #37 connection delay - 250ms"
    ]
  }
}
```

---

### POST /admin/grid/emergency

Initiate an emergency grid shutdown. Only `ADMIN` role can execute this action.

**Response (200):**
```json
{
  "success": true,
  "message": "Emergency shutdown initiated"
}
```

---

### GET /admin/contracts

List all deployed smart contracts on the blockchain.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "name": "EnergyToken",
      "address": "0xContractAddr001",
      "deployedAt": 1710000000000,
      "network": "sepolia"
    },
    {
      "name": "EnergyTrading",
      "address": "0xContractAddr002",
      "deployedAt": 1710100000000,
      "network": "sepolia"
    }
  ]
}
```

---

### POST /admin/contracts/deploy

Deploy a new smart contract to the blockchain.

**Request Body:**
```json
{
  "contractName": "RewardDistributor",
  "constructorArgs": ["0xTokenAddress", "100"]
}
```

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `contractName` | string | Yes | Name of the contract to deploy |
| `constructorArgs` | array | No | Constructor arguments for the contract |

**Response (201):** `ContractInfo` object with the deployed contract details.

---

## 9. Error Handling

All errors follow the standard response envelope:

```json
{
  "success": false,
  "error": "Descriptive error message"
}
```

### HTTP Status Codes

| Code | Description |
|------|-------------|
| `200` | Successful operation |
| `201` | Resource created successfully |
| `400` | Bad request — validation error or invalid input |
| `401` | Unauthorized — missing or invalid JWT token |
| `403` | Forbidden — insufficient permissions (role mismatch) |
| `404` | Resource not found |
| `500` | Internal server error |

### Common Error Messages

| Error | Status | Description |
|-------|--------|-------------|
| `"Invalid email or password"` | 400 | Login with wrong credentials |
| `"Email already registered"` | 400 | Registration with existing email |
| `"Invalid refresh token"` | 400 | Expired or invalid refresh token |
| `"Offer not found"` | 404 | Accessing a non-existent offer |
| `"Reward not found"` | 404 | Claiming a non-existent reward |
| `"Forbidden"` | 403 | Non-admin accessing admin endpoints |

---

## 10. Mock Test Credentials

The following test accounts are available in the mock environment:

| Email | Password | Role | User ID |
|-------|----------|------|---------|
| `consumer@smartgrid.az` | `password123` | CONSUMER | `user-001` |
| `prosumer@smartgrid.az` | `password456` | PROSUMER | `user-002` |
| `admin@smartgrid.az` | `password789` | ADMIN | `user-003` |
| `operator@smartgrid.az` | `password000` | GRID_OPERATOR | `user-004` |

> **Note:** In mock mode, passwords are stored in `hashed_<password>` format. The mock repository handles authentication internally.

---

## Appendix: Data Models

### User
```json
{
  "id": "string",
  "email": "string",
  "name": "string",
  "role": "CONSUMER | PROSUMER | GRID_OPERATOR | ADMIN",
  "walletAddress": "string | null",
  "createdAt": "long (timestamp)"
}
```

### EnergyOffer
```json
{
  "id": "string",
  "sellerId": "string",
  "sellerName": "string",
  "energyAmountKwh": "double",
  "pricePerKwh": "double",
  "energySource": "SOLAR | WIND | HYDRO | MIXED",
  "status": "ACTIVE | SOLD | CANCELLED | EXPIRED",
  "expiresAt": "long (timestamp)",
  "createdAt": "long (timestamp)"
}
```

### Transaction
```json
{
  "id": "string",
  "txHash": "string",
  "buyerId": "string",
  "sellerId": "string",
  "offerId": "string",
  "energyAmountKwh": "double",
  "totalPrice": "double",
  "type": "ENERGY_PURCHASE | TOKEN_TRANSFER | REWARD_CLAIM",
  "status": "PENDING | CONFIRMED | DELIVERED | SETTLED | FAILED",
  "createdAt": "long (timestamp)"
}
```

### SmartMeter
```json
{
  "id": "string",
  "userId": "string",
  "name": "string",
  "model": "string",
  "status": "ONLINE | OFFLINE | MAINTENANCE | ERROR",
  "lastReading": "double",
  "installedAt": "long (timestamp)"
}
```

### SmartMeterData
```json
{
  "meterId": "string",
  "userId": "string",
  "consumptionKwh": "double",
  "productionKwh": "double",
  "voltage": "double",
  "frequency": "double",
  "timestamp": "long (timestamp)"
}
```

### WalletInfo
```json
{
  "userId": "string",
  "walletAddress": "string",
  "ethBalance": "double",
  "energyTokenBalance": "double",
  "fiatEquivalentAzn": "double"
}
```

### Reward
```json
{
  "id": "string",
  "userId": "string",
  "amount": "double",
  "reason": "string",
  "claimed": "boolean",
  "createdAt": "long (timestamp)"
}
```

### EnergyAnalytics
```json
{
  "userId": "string",
  "period": "string",
  "totalConsumptionKwh": "double",
  "totalProductionKwh": "double",
  "avgDailyConsumption": "double",
  "avgDailyProduction": "double",
  "peakConsumptionKwh": "double",
  "dataPoints": "EnergyDataPoint[]"
}
```

### CarbonFootprint
```json
{
  "userId": "string",
  "totalCarbonSavedKg": "double",
  "treesEquivalent": "int",
  "greenEnergyPercentage": "double",
  "monthlyData": "CarbonDataPoint[]"
}
```

### GridHealth
```json
{
  "totalNodes": "int",
  "activeNodes": "int",
  "totalLoadKw": "double",
  "maxCapacityKw": "double",
  "healthPercentage": "double",
  "alerts": "string[]"
}
```

### ContractInfo
```json
{
  "name": "string",
  "address": "string",
  "deployedAt": "long (timestamp)",
  "network": "string"
}
```
