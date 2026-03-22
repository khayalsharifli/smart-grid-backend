# SmartGrid Backend API Documentation

**Base URL:** `http://localhost:8080/api/v1`
**Framework:** Ktor (Kotlin)
**Auth:** JWT Bearer Token

---

## 1. Landing Page API (Public - Auth teleb etmir)

Bu endpoint-ler landing page melumatlarini qaytarir. Heç bir JWT token teleb etmir.

### GET /landing
Butun landing page datasini bir requestle al.

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
Hero section datasi.

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
Why SmartGrid? - feature siyahisi.

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
Start Trading in Minutes - addimlar.

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
Platform statistikalari.

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
Call-to-action section.

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
Footer datasi (linkler, social, copyright).

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

## 2. Auth API

### POST /auth/register
Yeni istifadeci qeydiyyati.

**Request Body:**
```json
{
  "email": "user@example.com",
  "password": "securePassword123",
  "name": "Elvin Mammadov",
  "role": "CONSUMER"
}
```
**role enum:** `CONSUMER`, `PROSUMER`, `GRID_OPERATOR`, `ADMIN`

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

---

### POST /auth/login
JWT token al.

**Request Body:**
```json
{
  "email": "consumer@smartgrid.az",
  "password": "password123"
}
```

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

**Error (400):** `"Invalid email or password"`

---

### POST /auth/refresh
Token yenile.

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}
```

**Response (200):** Yeni accessToken ve refreshToken qaytarir.

---

### POST /auth/wallet/link 🔒
Ethereum wallet bagla.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "walletAddress": "0x1234abcd5678ef901234abcd5678ef90",
  "signature": "0xSignedMessage..."
}
```

**Response (200):** Yenilenmis User obyekti.

---

### GET /auth/profile 🔒
Istifadeci profili.

**Headers:** `Authorization: Bearer <token>`

**Response (200):**
```json
{
  "success": true,
  "data": {
    "id": "user-001",
    "email": "consumer@smartgrid.az",
    "name": "Elvin Mammadov",
    "role": "CONSUMER",
    "walletAddress": "0x1234abcd5678ef90",
    "createdAt": 1710000000000
  }
}
```

---

### PUT /auth/profile 🔒
Profil yenile.

**Headers:** `Authorization: Bearer <token>`

**Request Body:**
```json
{
  "name": "Elvin M.",
  "email": "elvin.new@smartgrid.az"
}
```

---

## 2. Energy API 🔒

Butun energy endpoint-leri JWT token teleb edir.

### GET /energy/consumption?period=daily
Istehlak datasi.

**Query Params:** `period` = `daily` | `weekly` | `monthly`

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "timestamp": 1711100000000,
      "consumptionKwh": 2.35,
      "productionKwh": 0.0
    }
  ]
}
```

---

### GET /energy/production?period=daily
Istehsal datasi (prosumer ucun).

**Query Params:** `period` = `daily` | `weekly` | `monthly`

**Response (200):** EnergyDataPoint array (timestamp, consumptionKwh, productionKwh)

---

### GET /energy/summary
Dashboard summary.

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
Enerji forecast (ML model).

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

## 3. Trading API 🔒

### GET /trading/offers
Aktiv elanlar siyahisi.

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
Elan detali.

**Response (200):** Tek EnergyOffer obyekti.

---

### POST /trading/offers
Yeni elan yarat.

**Request Body:**
```json
{
  "energyAmountKwh": 10.0,
  "pricePerKwh": 0.22,
  "energySource": "SOLAR",
  "durationHours": 24
}
```

**energySource enum:** `SOLAR`, `WIND`, `HYDRO`, `MIXED`

**Response (201):** Yaradilmis EnergyOffer obyekti.

---

### DELETE /trading/offers/{id}
Elani sil/legv et.

**Response (200):** `"Offer cancelled"`
**Error (404):** `"Offer not found"`

---

### POST /trading/buy
Enerji al (smart contract trigger).

**Request Body:**
```json
{
  "offerId": "offer-001",
  "buyerWalletAddress": "0x1234abcd5678ef90"
}
```

**Response (200):** Transaction obyekti (status: PENDING).

---

### GET /trading/history
Tranzaksiya tarixcesi.

**Response (200):** Transaction array.

---

### GET /trading/price
Cari dinamik qiymet.

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

**trend enum:** `RISING`, `FALLING`, `STABLE`

---

## 4. Wallet API 🔒

### GET /wallet/balance
Token + ETH balansi.

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
Wallet tranzaksiyalari.

**Response (200):** Transaction array.

---

### POST /wallet/transfer
Token transfer.

**Request Body:**
```json
{
  "toAddress": "0xabcd1234ef567890...",
  "amount": 50.0
}
```

**Response (200):** Transaction obyekti (status: PENDING).

---

### GET /wallet/rewards
Reward tarixcesi.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "reward-001",
      "userId": "user-001",
      "amount": 10.0,
      "reason": "Enerji qenayeti bonusu - Yanvar",
      "claimed": false,
      "createdAt": 1708500000000
    }
  ]
}
```

---

### POST /wallet/claim-reward?rewardId=reward-001
Reward claim et.

**Query Params:** `rewardId` (required)

**Response (200):** Claimed Reward obyekti (claimed: true).

---

## 5. Meter API 🔒

### GET /meters
Qosulmus cihazlar.

**Response (200):**
```json
{
  "success": true,
  "data": [
    {
      "id": "meter-001",
      "userId": "user-001",
      "name": "Ev sayqaci",
      "model": "ABB A44",
      "status": "ONLINE",
      "lastReading": 2.4,
      "installedAt": 1710000000000
    }
  ]
}
```

**status enum:** `ONLINE`, `OFFLINE`, `MAINTENANCE`, `ERROR`

---

### POST /meters
Yeni cihaz elave et.

**Request Body:**
```json
{
  "name": "Yeni sayqac",
  "model": "Schneider iEM3155"
}
```

**Response (201):** SmartMeter obyekti.

---

### DELETE /meters/{id}
Cihaz sil.

**Response (200):** `"Meter deleted"`

---

### GET /meters/{id}/data
Cihaz datasi (son 60 deqiqe).

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

### WS /api/v1/meters/stream
Real-time WebSocket stream (her 2 saniye SmartMeterData gondorilir).

---

## 6. Analytics API 🔒

### GET /analytics/energy?period=monthly
Enerji trend analizi.

**Query Params:** `period` = `daily` | `weekly` | `monthly`

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
Carbon footprint.

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

### GET /analytics/comparison?period1=2026-02&period2=2026-03
Dovr muqayisesi.

**Response (200):** Map<String, EnergyAnalytics> - her iki dovr ucun analytics.

---

## 7. Admin API 🔒 (ADMIN rolu teleb olunur)

### GET /admin/users
Butun istifadeciler.

**Response (200):** User array.

---

### PUT /admin/users/{id}/role
Rol deyisdir.

**Request Body:**
```json
{
  "role": "PROSUMER"
}
```

**Response (200):** Yenilenmis User obyekti.

---

### PUT /admin/users/{id}/block
Istifadecini blokla.

**Request Body:**
```json
{
  "blocked": true,
  "reason": "Suspicious activity"
}
```

**Response (200):** User obyekti.

---

### GET /admin/grid/health
Grid saglamliq statusu. (ADMIN ve GRID_OPERATOR)

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
      "Node #12 yuksek istehlak - 4.8kW",
      "Node #37 baglanti kecikmes - 250ms"
    ]
  }
}
```

---

### POST /admin/grid/emergency
Emergency shutdown.

**Response (200):** `"Emergency shutdown initiated"`

---

### GET /admin/contracts
Deploy edilmis smart contracts.

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
    }
  ]
}
```

---

### POST /admin/contracts/deploy
Yeni contract deploy.

**Request Body:**
```json
{
  "contractName": "RewardDistributor",
  "constructorArgs": ["0xTokenAddress", "100"]
}
```

**Response (201):** ContractInfo obyekti.

---

## Error Response Formati

Butun xetalar eyni formatda qaytarilir:

```json
{
  "success": false,
  "error": "Error message here"
}
```

**Status Codes:**
| Code | Sebebi |
|------|--------|
| 200 | Ugurlu emeliyyat |
| 201 | Resurs yaradildi |
| 400 | Yanlis request / validation xetasi |
| 401 | Token yoxdur ve ya etibarsizdir |
| 403 | Icaze yoxdur (rol uygunsuzlugu) |
| 404 | Resurs tapilmadi |
| 500 | Server xetasi |

---

## Mock Test Credentials

| Email | Password | Role |
|-------|----------|------|
| consumer@smartgrid.az | password123 | CONSUMER |
| prosumer@smartgrid.az | password456 | PROSUMER |
| admin@smartgrid.az | password789 | ADMIN |
| operator@smartgrid.az | password000 | GRID_OPERATOR |

**Qeyd:** Mock rejimde sifre hash-i `hashed_<password>` formatindadir. Login ucun `hashed_password_123` seklinde saxlanilir, ona gore mock datada duzgun isleyir.
