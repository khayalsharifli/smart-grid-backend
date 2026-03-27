# SmartGrid — Decentralized Energy Trading Platform

A blockchain-integrated peer-to-peer energy trading platform built with Ktor (Kotlin), enabling consumers and prosumers to buy, sell, and trade renewable energy directly — powered by Ethereum smart contracts.

---

## Table of Contents

- [Overview](#overview)
- [Key Features](#key-features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Application Layer Architecture](#application-layer-architecture)
- [API Module Overview](#api-module-overview)
- [Request Flow](#request-flow)
- [Blockchain Architecture](#blockchain-architecture)
- [Smart Contract Interaction Flow](#smart-contract-interaction-flow)
- [Data Architecture](#data-architecture)
- [Real-time Data Pipeline](#real-time-data-pipeline)
- [User Roles & Permissions](#user-roles--permissions)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)

---

## Overview

SmartGrid is a next-generation energy trading platform that connects energy producers (prosumers) with consumers through a decentralized marketplace. The platform leverages Ethereum blockchain for transparent, trustless energy transactions while providing real-time monitoring through IoT smart meter integration.

```
┌─────────────────────────────────────────────────────────────────┐
│                      SMARTGRID PLATFORM                         │
│                                                                 │
│   ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐ │
│   │ Consumer │◄──►│  P2P     │◄──►│ Prosumer │    │   Grid   │ │
│   │          │    │  Energy  │    │  (Solar/ │    │ Operator │ │
│   │  Buys    │    │  Market  │    │   Wind)  │    │          │ │
│   │  Energy  │    │          │    │  Sells   │    │ Monitors │ │
│   └──────────┘    └──────────┘    │  Energy  │    │ & Settles│ │
│                         │         └──────────┘    └──────────┘ │
│                         │                                       │
│              ┌──────────▼──────────┐                           │
│              │   Ethereum          │                            │
│              │   Blockchain        │                            │
│              │   (Smart Contracts) │                            │
│              └─────────────────────┘                           │
└─────────────────────────────────────────────────────────────────┘
```

---

## Key Features

- **Peer-to-Peer Energy Trading** — Direct energy marketplace between producers and consumers
- **Blockchain Transparency** — All trades recorded on Ethereum with smart contracts
- **Smart Meter Integration** — Real-time IoT monitoring via WebSocket streaming
- **Dynamic Pricing** — Supply/demand-based market pricing engine
- **Energy Analytics** — Consumption/production trends, forecasts, and comparisons
- **Carbon Tracking** — Environmental impact monitoring with carbon savings metrics
- **Token Rewards** — Incentives for energy savings and green energy usage
- **Decentralized Governance** — Community voting on platform parameters
- **Role-Based Access** — Consumer, Prosumer, Grid Operator, and Admin roles

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| **Language** | Kotlin 2.3.10 |
| **Backend Framework** | Ktor 3.1.2 |
| **Authentication** | JWT (HS256) via Auth0 library |
| **Real-time** | Ktor WebSockets |
| **Blockchain** | Ethereum (Sepolia Testnet) |
| **Smart Contracts** | Solidity ^0.8.20 (OpenZeppelin) |
| **Blockchain SDK** | Web3j |
| **Build Tool** | Gradle (Kotlin DSL) |
| **JVM** | Java 22 |
| **Logging** | Logback |
| **Database (planned)** | PostgreSQL + TimescaleDB + Redis |

---

## System Architecture

The platform follows a multi-tier architecture connecting clients, backend services, blockchain, and IoT devices:

```
┌────────────────────────────────────────────────────────────────────────────┐
│                              CLIENTS                                       │
│                                                                            │
│   ┌────────────┐    ┌────────────┐    ┌────────────┐    ┌──────────────┐  │
│   │  Web App   │    │ Mobile App │    │  Admin     │    │  3rd Party   │  │
│   │  (React)   │    │ (Flutter)  │    │  Dashboard │    │  Integrations│  │
│   └─────┬──────┘    └─────┬──────┘    └─────┬──────┘    └──────┬───────┘  │
│         │                 │                 │                   │          │
└─────────┼─────────────────┼─────────────────┼───────────────────┼──────────┘
          │    REST API     │    REST API     │     REST API      │
          │    WebSocket    │                 │                   │
          ▼                 ▼                 ▼                   ▼
┌────────────────────────────────────────────────────────────────────────────┐
│                         API GATEWAY (Ktor)                                 │
│                      http://localhost:8080/api/v1                          │
│                                                                            │
│   ┌──────────┐  ┌──────────┐  ┌───────────┐  ┌──────────┐  ┌──────────┐ │
│   │   CORS   │  │   JWT    │  │  Status   │  │  Route   │  │WebSocket │ │
│   │  Plugin  │  │  Auth    │  │  Pages    │  │  Config  │  │  Plugin  │ │
│   └──────────┘  └──────────┘  └───────────┘  └──────────┘  └──────────┘ │
└────────────────────────────────┬───────────────────────────────────────────┘
                                 │
                                 ▼
┌────────────────────────────────────────────────────────────────────────────┐
│                          SERVICE LAYER                                     │
│                                                                            │
│  ┌───────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────────────┐ │
│  │   Auth    │ │  Energy  │ │ Trading  │ │  Wallet  │ │   Analytics   │ │
│  │  Service  │ │  Service │ │  Service │ │  Service │ │    Service    │ │
│  └───────────┘ └──────────┘ └──────────┘ └──────────┘ └───────────────┘ │
│  ┌───────────┐ ┌──────────┐ ┌──────────┐                                │
│  │  Meter    │ │  Admin   │ │ Landing  │                                │
│  │  Service  │ │  Service │ │  Service │                                │
│  └───────────┘ └──────────┘ └──────────┘                                │
└────────────────────────────────┬───────────────────────────────────────────┘
                                 │
                    ┌────────────┼────────────┐
                    ▼            ▼            ▼
┌──────────────┐ ┌──────────┐ ┌──────────────────────┐
│  Repository  │ │  Web3j   │ │     IoT Gateway      │
│    Layer     │ │  Client  │ │    (Smart Meters)     │
│  (Database)  │ │          │ │                       │
└──────┬───────┘ └────┬─────┘ └───────────┬───────────┘
       │              │                   │
       ▼              ▼                   ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────────┐
│ PostgreSQL   │ │  Ethereum    │ │   Smart Meter    │
│ TimescaleDB  │ │  Blockchain  │ │   Devices (IoT)  │
│ Redis        │ │  (Sepolia)   │ │                  │
└──────────────┘ └──────────────┘ └──────────────────┘
```

---

## Application Layer Architecture

The application follows Clean Architecture principles with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                    │
│                    (routes/ package)                     │
│                                                         │
│   LandingRoutes  AuthRoutes  EnergyRoutes  TradingRoutes│
│   WalletRoutes   MeterRoutes  AnalyticsRoutes  AdminRoutes│
└────────────────────────┬────────────────────────────────┘
                         │ calls
                         ▼
┌─────────────────────────────────────────────────────────┐
│                     DOMAIN LAYER                        │
│                                                         │
│  ┌─────────────────────────────────────────────────┐   │
│  │              SERVICE LAYER                       │   │
│  │         (domain/service/ package)                │   │
│  │                                                  │   │
│  │  AuthService    EnergyService    TradingService  │   │
│  │  WalletService  MeterService     AnalyticsService│   │
│  │  AdminService   LandingService                   │   │
│  └────────────────────┬────────────────────────────┘   │
│                       │ uses                            │
│  ┌────────────────────▼────────────────────────────┐   │
│  │           REPOSITORY INTERFACES                  │   │
│  │        (domain/repository/ package)              │   │
│  │                                                  │   │
│  │  AuthRepository     EnergyRepository             │   │
│  │  TradingRepository  WalletRepository             │   │
│  │  MeterRepository    AnalyticsRepository          │   │
│  │  AdminRepository    LandingRepository            │   │
│  └──────────────────────────────────────────────────┘   │
│                                                         │
│  ┌──────────────────────────────────────────────────┐   │
│  │              DATA MODELS & DTOs                   │   │
│  │  (domain/model/ & domain/dto/ packages)          │   │
│  │                                                  │   │
│  │  User, EnergyOffer, Transaction, SmartMeter      │   │
│  │  WalletInfo, Reward, EnergyAnalytics, GridHealth │   │
│  │  RegisterRequest, LoginRequest, AuthResponse...  │   │
│  └──────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────┘
                         │ implements
                         ▼
┌─────────────────────────────────────────────────────────┐
│                      DATA LAYER                         │
│                  (data/mock/ package)                    │
│                                                         │
│  MockAuthRepository      MockEnergyRepository          │
│  MockTradingRepository   MockWalletRepository          │
│  MockMeterRepository     MockAnalyticsRepository       │
│  MockAdminRepository     MockLandingRepository         │
│                                                         │
│  (In-memory implementations — ready for DB swap)       │
└─────────────────────────────────────────────────────────┘
```

---

## API Module Overview

The platform exposes 35+ REST endpoints organized into 8 modules:

```
/api/v1
│
├── /landing              [PUBLIC]     Landing page content (7 endpoints)
│   ├── GET /                          Full page data
│   ├── GET /hero                      Hero section
│   ├── GET /features                  Features list
│   ├── GET /how-it-works              Onboarding steps
│   ├── GET /stats                     Platform statistics
│   ├── GET /cta                       Call-to-action
│   └── GET /footer                    Footer & links
│
├── /auth                 [MIXED]      Authentication (6 endpoints)
│   ├── POST /register                 Create account
│   ├── POST /login                    Get JWT tokens
│   ├── POST /refresh                  Refresh access token
│   ├── POST /wallet/link    🔒        Link Ethereum wallet
│   ├── GET  /profile        🔒        Get user profile
│   └── PUT  /profile        🔒        Update profile
│
├── /energy               [AUTH 🔒]    Energy monitoring (4 endpoints)
│   ├── GET /consumption               Consumption history
│   ├── GET /production                Production history
│   ├── GET /summary                   Dashboard summary
│   └── GET /forecast                  24h ML forecast
│
├── /trading              [AUTH 🔒]    Marketplace (7 endpoints)
│   ├── GET    /offers                 List active offers
│   ├── GET    /offers/{id}            Offer details
│   ├── POST   /offers                 Create offer
│   ├── DELETE /offers/{id}            Cancel offer
│   ├── POST   /buy                    Purchase energy
│   ├── GET    /history                Trade history
│   └── GET    /price                  Dynamic market price
│
├── /wallet               [AUTH 🔒]    Token & wallet (5 endpoints)
│   ├── GET  /balance                  ETH & token balance
│   ├── GET  /transactions             Transaction history
│   ├── POST /transfer                 Transfer tokens
│   ├── GET  /rewards                  Reward history
│   └── POST /claim-reward             Claim pending reward
│
├── /meters               [AUTH 🔒]    Smart meters (4+1 endpoints)
│   ├── GET    /                       List meters
│   ├── POST   /                       Add meter
│   ├── DELETE /{id}                   Remove meter
│   ├── GET    /{id}/data              Meter readings (60min)
│   └── WS     /stream                 Real-time WebSocket stream
│
├── /analytics            [AUTH 🔒]    Analytics (3 endpoints)
│   ├── GET /energy                    Energy trends
│   ├── GET /carbon                    Carbon footprint
│   └── GET /comparison                Period comparison
│
└── /admin                [ADMIN 🔒]   Administration (7 endpoints)
    ├── GET  /users                    List all users
    ├── PUT  /users/{id}/role          Change user role
    ├── PUT  /users/{id}/block         Block/unblock user
    ├── GET  /grid/health              Grid health status
    ├── POST /grid/emergency           Emergency shutdown
    ├── GET  /contracts                Deployed contracts
    └── POST /contracts/deploy         Deploy new contract
```

---

## Request Flow

### Authentication Flow

```
┌────────┐                    ┌────────┐                    ┌──────────┐
│ Client │                    │  Ktor  │                    │   Auth   │
│        │                    │ Server │                    │ Service  │
└───┬────┘                    └───┬────┘                    └────┬─────┘
    │                             │                              │
    │  POST /auth/login           │                              │
    │  {email, password}          │                              │
    │────────────────────────────►│                              │
    │                             │  validate credentials       │
    │                             │─────────────────────────────►│
    │                             │                              │
    │                             │  generate JWT tokens         │
    │                             │◄─────────────────────────────│
    │                             │                              │
    │  {accessToken, refreshToken,│                              │
    │   expiresIn, userId}        │                              │
    │◄────────────────────────────│                              │
    │                             │                              │
    │  GET /energy/summary        │                              │
    │  Authorization: Bearer xxx  │                              │
    │────────────────────────────►│                              │
    │                             │  verify JWT                  │
    │                             │  extract userId, role        │
    │                             │─────────────────────────────►│
    │                             │                              │
    │  {dashboardSummary}         │                              │
    │◄────────────────────────────│                              │
    │                             │                              │
```

### Energy Trading Flow

```
┌──────────┐    ┌────────┐    ┌──────────┐    ┌───────────┐    ┌────────────┐
│ Prosumer │    │  Ktor  │    │ Trading  │    │ Blockchain│    │  Consumer  │
│ (Seller) │    │ Server │    │ Service  │    │ (Sepolia) │    │  (Buyer)   │
└────┬─────┘    └───┬────┘    └────┬─────┘    └─────┬─────┘    └─────┬──────┘
     │              │              │                │                 │
     │ POST /trading/offers        │                │                 │
     │ {amount, price, source}     │                │                 │
     │─────────────►│              │                │                 │
     │              │─────────────►│                │                 │
     │              │              │  createOffer() │                 │
     │              │              │───────────────►│                 │
     │              │              │  OfferCreated  │                 │
     │              │              │◄───────────────│                 │
     │  offer-001   │              │                │                 │
     │◄─────────────│              │                │                 │
     │              │              │                │                 │
     │              │              │                │  GET /trading/offers
     │              │              │                │◄────────────────│
     │              │              │                │                 │
     │              │              │                │  [offer-001...] │
     │              │              │                │────────────────►│
     │              │              │                │                 │
     │              │              │                │  POST /trading/buy
     │              │              │                │  {offerId, wallet}
     │              │              │                │◄────────────────│
     │              │              │  buyEnergy()   │                 │
     │              │              │───────────────►│                 │
     │              │              │  token escrow  │                 │
     │              │              │◄───────────────│                 │
     │              │              │                │  Transaction    │
     │              │              │                │  (PENDING)      │
     │              │              │                │────────────────►│
     │              │              │                │                 │
```

---

## Blockchain Architecture

### Smart Contract Ecosystem

```
┌─────────────────────────────────────────────────────────────────────┐
│                    ETHEREUM BLOCKCHAIN (Sepolia)                     │
│                                                                     │
│  ┌────────────────────────────────────────────────────────────────┐ │
│  │                    GridGovernance.sol                           │ │
│  │              Decentralized Voting & Governance                 │ │
│  │         (proposals, voting, parameter changes)                │ │
│  └───────────────────────┬────────────────────────────────────────┘ │
│                          │ governs                                  │
│                          ▼                                          │
│  ┌──────────────────────────────────────────────────────────────┐  │
│  │                                                              │  │
│  │  ┌─────────────────┐    ┌─────────────────────────────────┐ │  │
│  │  │  EnergyToken    │◄───│       EnergyTrading.sol         │ │  │
│  │  │    .sol         │    │                                 │ │  │
│  │  │                 │    │  ┌──────────┐   ┌────────────┐  │ │  │
│  │  │  SGET (ERC-20)  │    │  │  Create  │──►│   Active   │  │ │  │
│  │  │  1B max supply  │    │  │  Offer   │   │   Offers   │  │ │  │
│  │  │                 │    │  └──────────┘   └─────┬──────┘  │ │  │
│  │  │  • mint         │    │                       │         │ │  │
│  │  │  • burn         │    │                 ┌─────▼──────┐  │ │  │
│  │  │  • transfer     │    │                 │  Buy       │  │ │  │
│  │  │  • approve      │    │                 │  Energy    │  │ │  │
│  │  │                 │    │                 └─────┬──────┘  │ │  │
│  │  │                 │    │                       │         │ │  │
│  │  │                 │    │                 ┌─────▼──────┐  │ │  │
│  │  │                 │◄───│                 │  Settle    │  │ │  │
│  │  │  token transfer │    │                 │  (Operator)│  │ │  │
│  │  │                 │    │                 └────────────┘  │ │  │
│  │  └─────────────────┘    └─────────────────────────────────┘ │  │
│  │                                                              │  │
│  │  ┌─────────────────┐    ┌─────────────────────────────────┐ │  │
│  │  │ SmartMeter      │    │     RewardDistributor.sol       │ │  │
│  │  │ Registry.sol    │    │                                 │ │  │
│  │  │                 │    │  • Low consumption bonus (5 SGET)│ │  │
│  │  │  • register     │    │  • Solar production    (10 SGET)│ │  │
│  │  │  • submitHash   │    │  • Peak avoidance      (3 SGET) │ │  │
│  │  │  • verify       │    │  • 7-day streak        (25 SGET)│ │  │
│  │  │  • deactivate   │    │  • First trade         (50 SGET)│ │  │
│  │  └─────────────────┘    └─────────────────────────────────┘ │  │
│  │                                                              │  │
│  └──────────────────────────────────────────────────────────────┘  │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Smart Contract Interaction Flow

### Complete Energy Purchase Lifecycle

```
┌──────────┐         ┌─────────────┐         ┌──────────────┐        ┌───────────┐
│  Buyer   │         │EnergyToken  │         │EnergyTrading │        │  Grid     │
│  (User)  │         │  Contract   │         │  Contract    │        │ Operator  │
└────┬─────┘         └──────┬──────┘         └──────┬───────┘        └─────┬─────┘
     │                      │                       │                      │
     │  1. approve(Trading, │                       │                      │
     │     totalPrice)      │                       │                      │
     │─────────────────────►│                       │                      │
     │                      │                       │                      │
     │  2. buyEnergy(offerId)                       │                      │
     │─────────────────────────────────────────────►│                      │
     │                      │                       │                      │
     │                      │  3. transferFrom      │                      │
     │                      │     (buyer→contract)  │                      │
     │                      │◄──────────────────────│                      │
     │                      │                       │                      │
     │                      │  4. tokens held       │                      │
     │                      │     in escrow         │                      │
     │                      │──────────────────────►│                      │
     │                      │                       │                      │
     │  5. EnergyPurchased  │                       │                      │
     │     event emitted    │                       │                      │
     │◄─────────────────────────────────────────────│                      │
     │                      │                       │                      │
     │                      │                       │  6. verify delivery  │
     │                      │                       │  settleTransaction() │
     │                      │                       │◄─────────────────────│
     │                      │                       │                      │
     │                      │  7. transfer          │                      │
     │                      │     (→seller: 98%)    │                      │
     │                      │◄──────────────────────│                      │
     │                      │                       │                      │
     │                      │  8. transfer          │                      │
     │                      │     (→fee: 2%)        │                      │
     │                      │◄──────────────────────│                      │
     │                      │                       │                      │
     │  9. TransactionSettled event                 │                      │
     │◄─────────────────────────────────────────────│                      │
     │                      │                       │                      │
```

---

## Data Architecture

### On-chain vs Off-chain Data Split

```
┌─────────────────────────────────────────────────────────────────┐
│                     DATA ARCHITECTURE                           │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                ON-CHAIN (Ethereum)                        │  │
│  │                                                           │  │
│  │  • Token balances & transfers (ERC-20)                   │  │
│  │  • Energy offers & purchase transactions                 │  │
│  │  • Smart meter registration & data hashes                │  │
│  │  • Reward distribution records                           │  │
│  │  • Governance proposals & votes                          │  │
│  │                                                           │  │
│  │  WHY: Immutability, transparency, trustless verification │  │
│  └───────────────────────────────────────────────────────────┘  │
│                              │                                  │
│                    Event Sync│(Web3j listener)                  │
│                              ▼                                  │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │              OFF-CHAIN (PostgreSQL + Redis)               │  │
│  │                                                           │  │
│  │  PostgreSQL:                                              │  │
│  │  • User profiles (email, name, password hash)            │  │
│  │  • Notification history                                  │  │
│  │  • IoT device configuration                              │  │
│  │                                                           │  │
│  │  TimescaleDB (PostgreSQL extension):                     │  │
│  │  • Raw smart meter time-series data                      │  │
│  │  • Aggregated analytics & reports                        │  │
│  │                                                           │  │
│  │  Redis:                                                   │  │
│  │  • JWT/Session token cache                               │  │
│  │  • Real-time streaming buffer                            │  │
│  │  • Rate limiting counters                                │  │
│  │                                                           │  │
│  │  WHY: Performance, privacy (GDPR), high-volume data     │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
│  ┌───────────────────────────────────────────────────────────┐  │
│  │                IPFS (Decentralized Storage)              │  │
│  │                                                           │  │
│  │  • Meter data raw batch files                            │  │
│  │  • Exported PDF/CSV reports                              │  │
│  │  • Smart contract metadata & ABI                         │  │
│  │                                                           │  │
│  │  WHY: Large files too expensive for blockchain storage   │  │
│  └───────────────────────────────────────────────────────────┘  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Real-time Data Pipeline

### Smart Meter → Dashboard Flow

```
┌───────────┐     ┌───────────┐     ┌───────────┐     ┌───────────┐
│  Smart    │     │    IoT    │     │   Ktor    │     │  Client   │
│  Meter    │     │  Gateway  │     │  Backend  │     │  (Web/    │
│  (Device) │     │           │     │           │     │  Mobile)  │
└─────┬─────┘     └─────┬─────┘     └─────┬─────┘     └─────┬─────┘
      │                 │                 │                   │
      │  sensor data    │                 │                   │
      │  (2s interval)  │                 │                   │
      │────────────────►│                 │                   │
      │                 │                 │                   │
      │                 │  MQTT/HTTP      │                   │
      │                 │────────────────►│                   │
      │                 │                 │                   │
      │                 │                 │──┐ Store in       │
      │                 │                 │  │ TimescaleDB    │
      │                 │                 │◄─┘                │
      │                 │                 │                   │
      │                 │                 │  WebSocket push   │
      │                 │                 │  (SmartMeterData) │
      │                 │                 │──────────────────►│
      │                 │                 │                   │
      │                 │                 │──┐ Compute hash   │
      │                 │                 │  │ (hourly batch) │
      │                 │                 │◄─┘                │
      │                 │                 │                   │
      │                 │                 │  submitReading()  │
      │                 │                 │───────────┐       │
      │                 │                 │           │       │
      │                 │                 │  Blockchain       │
      │                 │                 │  (hash stored)    │
      │                 │                 │◄──────────┘       │
      │                 │                 │                   │
```

---

## User Roles & Permissions

```
┌─────────────────────────────────────────────────────────────────┐
│                    ROLE-BASED ACCESS CONTROL                    │
│                                                                 │
│  ┌─────────────┐  ┌─────────────┐  ┌────────────┐  ┌────────┐ │
│  │  CONSUMER   │  │  PROSUMER   │  │    GRID    │  │ ADMIN  │ │
│  │             │  │             │  │  OPERATOR  │  │        │ │
│  └──────┬──────┘  └──────┬──────┘  └─────┬──────┘  └───┬────┘ │
│         │                │               │              │      │
│  • View energy    • All Consumer   • All Consumer  • All      │
│    consumption      permissions      permissions    permissions│
│  • Buy energy     • Create energy  • Settle        • User     │
│    from market      offers           transactions    management│
│  • View wallet    • View            • Grid health   • Role    │
│    balance          production        monitoring      changes  │
│  • Claim          • Manage          • Contract       • Block/ │
│    rewards          smart meters      deployment      unblock  │
│  • View           • Earn                             • Emergency│
│    analytics        producer                           shutdown │
│                     rewards                           • Contract│
│                                                        deploy  │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Getting Started

### Prerequisites

- **JDK 22** or higher
- **Gradle** (included via wrapper)
- **Git**

### Run the Server

```bash
# Clone the repository
git clone <repository-url>
cd smartgrid

# Build the project
./gradlew build

# Run the server
./gradlew run
```

The server starts at `http://localhost:8080`.

### Quick Test

```bash
# Login with test credentials
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "consumer@smartgrid.az", "password": "password123"}'

# Use the returned accessToken for authenticated requests
curl http://localhost:8080/api/v1/energy/summary \
  -H "Authorization: Bearer <access_token>"
```

### Test Accounts

| Email | Password | Role |
|-------|----------|------|
| `consumer@smartgrid.az` | `password123` | CONSUMER |
| `prosumer@smartgrid.az` | `password456` | PROSUMER |
| `admin@smartgrid.az` | `password789` | ADMIN |
| `operator@smartgrid.az` | `password000` | GRID_OPERATOR |

---

## API Documentation

For complete API reference with request/response examples, see:

- **[BACKEND-API-DOCS.md](BACKEND-API-DOCS.md)** — Full REST API documentation (35+ endpoints)
- **[BLOCKCHAIN-ARCHITECTURE.md](BLOCKCHAIN-ARCHITECTURE.md)** — Smart contract specifications and blockchain integration

---

## Project Structure

```
smartgrid/
├── src/main/kotlin/com/smartgrid/
│   ├── Application.kt                    # Entry point, DI setup, module config
│   │
│   ├── plugins/                           # Ktor plugins
│   │   ├── Routing.kt                     # Route registration
│   │   ├── Security.kt                    # JWT authentication config
│   │   ├── CORS.kt                        # CORS configuration
│   │   ├── StatusPages.kt                 # Error handling
│   │   └── WebSockets.kt                  # WebSocket configuration
│   │
│   ├── routes/                            # API route handlers
│   │   ├── LandingRoutes.kt               # GET /landing/*
│   │   ├── AuthRoutes.kt                  # POST /auth/*
│   │   ├── EnergyRoutes.kt                # GET /energy/*
│   │   ├── TradingRoutes.kt               # /trading/*
│   │   ├── WalletRoutes.kt                # /wallet/*
│   │   ├── MeterRoutes.kt                 # /meters/*
│   │   ├── AnalyticsRoutes.kt             # /analytics/*
│   │   └── AdminRoutes.kt                 # /admin/*
│   │
│   ├── domain/
│   │   ├── dto/                           # Request/Response DTOs
│   │   │   ├── AuthDtos.kt                # Register, Login, Refresh DTOs
│   │   │   ├── TradingDtos.kt             # Create offer, Buy energy DTOs
│   │   │   ├── WalletDtos.kt              # Transfer, Reward DTOs
│   │   │   ├── MeterDtos.kt               # Add meter DTOs
│   │   │   └── AdminDtos.kt               # Role change, Block user DTOs
│   │   │
│   │   ├── model/                         # Domain entities & enums
│   │   │   ├── User.kt                    # User, UserRole
│   │   │   ├── EnergyModels.kt            # EnergyDataPoint, DashboardSummary, Forecast
│   │   │   ├── TradingModels.kt           # EnergyOffer, Transaction, DynamicPrice
│   │   │   ├── WalletModels.kt            # WalletInfo, Reward
│   │   │   ├── MeterModels.kt             # SmartMeter, SmartMeterData
│   │   │   ├── AnalyticsModels.kt         # EnergyAnalytics, CarbonFootprint
│   │   │   └── AdminModels.kt             # GridHealth, ContractInfo
│   │   │
│   │   ├── service/                       # Business logic
│   │   │   ├── AuthService.kt
│   │   │   ├── EnergyService.kt
│   │   │   ├── TradingService.kt
│   │   │   ├── WalletService.kt
│   │   │   ├── MeterService.kt
│   │   │   ├── AnalyticsService.kt
│   │   │   ├── AdminService.kt
│   │   │   └── LandingService.kt
│   │   │
│   │   └── repository/                    # Repository interfaces
│   │       ├── AuthRepository.kt
│   │       ├── EnergyRepository.kt
│   │       ├── TradingRepository.kt
│   │       ├── WalletRepository.kt
│   │       ├── MeterRepository.kt
│   │       ├── AnalyticsRepository.kt
│   │       ├── AdminRepository.kt
│   │       └── LandingRepository.kt
│   │
│   ├── data/
│   │   └── mock/                          # Mock repository implementations
│   │       ├── MockAuthRepository.kt
│   │       ├── MockEnergyRepository.kt
│   │       ├── MockTradingRepository.kt
│   │       ├── MockWalletRepository.kt
│   │       ├── MockMeterRepository.kt
│   │       ├── MockAnalyticsRepository.kt
│   │       ├── MockAdminRepository.kt
│   │       └── MockLandingRepository.kt
│   │
│   └── util/
│       ├── Constants.kt                   # Route paths, params, error messages
│       └── JwtConfig.kt                   # JWT token generation & validation
│
├── src/main/resources/
│   ├── application.conf                   # Ktor server configuration
│   └── logback.xml                        # Logging configuration
│
├── build.gradle.kts                       # Gradle build configuration
├── settings.gradle.kts                    # Gradle settings
├── BACKEND-API-DOCS.md                    # Complete API reference
├── BLOCKCHAIN-ARCHITECTURE.md             # Smart contract architecture
└── README.md                              # This file
```

---

## License

Copyright (c) 2026 SmartGrid Platform. All rights reserved.
