# SmartGrid Blockchain & Smart Contract Arxitekturası

**Network:** Ethereum (Sepolia Testnet → Mainnet)
**Dil:** Solidity ^0.8.20
**Framework:** Hardhat
**Token Standart:** ERC-20 (OpenZeppelin)
**Backend Inteqrasiya:** Web3j (Ktor)

---

## 1. On-chain vs Off-chain Data Bolgusu

### ON-CHAIN (Blockchain-de saxlanilir)

| Data | Contract | Sebeb |
|------|----------|-------|
| EnergyToken balanslari | EnergyToken.sol | Decentralized ownership, ERC-20 standart |
| Token transferleri (mint/burn/transfer) | EnergyToken.sol | Seffafliq, audit trail |
| Enerji elanları (offers) | EnergyTrading.sol | Trustless marketplace, double-sale prevention |
| Enerji alis-veris tranzaksiyalari | EnergyTrading.sol | Deyisilmezlik, seffafliq |
| Offer statusu (Active/Sold/Cancelled) | EnergyTrading.sol | On-chain state tracking |
| Smart meter qeydiyyati | SmartMeterRegistry.sol | Tamper-proof cihaz registry |
| Meter data HASH-leri | SmartMeterRegistry.sol | Verification (raw data off-chain) |
| Reward distribution | RewardDistributor.sol | Serf tokenomics, audit |
| Governance teblikleri ve sesverme | GridGovernance.sol | Decentralized decision making |

### OFF-CHAIN (PostgreSQL/Redis-de saxlanilir)

| Data | Harada | Sebeb |
|------|--------|-------|
| User profil (email, name, password) | PostgreSQL | Privacy, GDPR, sik deyisir |
| JWT/Session tokenleri | Redis | Muveqqeti, suretli access |
| Raw smart meter datasi | PostgreSQL + TimescaleDB | Hecm boyukdur, gas fee bahdir |
| Real-time streaming data | Redis + WebSocket | Latency teleb olunur |
| Analytics/Reports (aggregated) | TimescaleDB | Query performance |
| Notification tarixcesi | PostgreSQL | Blockchain-e aid deyil |
| IoT cihaz konfiqurasiyasi | PostgreSQL | Sik yenilenme |

### IPFS (Fayl saxlama)

| Data | Sebeb |
|------|-------|
| Meter data raw batch-leri | Boyuk fayl, blockchain-e yazmaq bahdir |
| PDF/CSV hesabatlari | Export edilmis fayllar |
| Contract metadata | ABI, deploy info |

---

## 2. Smart Contract-lar

### 2.1. EnergyToken.sol (ERC-20)

**Meqsed:** Platformanin enerji tokeni. Enerji alis-verisi ve mukafatlar ucun istifade olunur.

**OpenZeppelin extends:** ERC20, ERC20Burnable, Ownable, AccessControl

**Rollar:**
- `MINTER_ROLE` - token mint ede biler (Grid Operator)
- `DEFAULT_ADMIN_ROLE` - rollari idare edir (Admin)

**State Variables:**
```solidity
string public constant NAME = "SmartGrid Energy Token";
string public constant SYMBOL = "SGET";
uint8 public constant DECIMALS = 18;
uint256 public constant MAX_SUPPLY = 1_000_000_000 * 10**18; // 1 milyard
```

**Funksiyalar:**

| Funksiya | Gosterici | Tesvir |
|----------|-----------|--------|
| `mint(address to, uint256 amount)` | onlyRole(MINTER_ROLE) | Yeni token yaradir |
| `burn(uint256 amount)` | public | Oz tokenini yandiri |
| `transfer(address to, uint256 amount)` | public | Token transfer |
| `approve(address spender, uint256 amount)` | public | Marketplace ucun approval |
| `transferFrom(address from, address to, uint256 amount)` | public | Approval-dan sonra transfer |
| `balanceOf(address account)` | view | Balans oxuma |
| `totalSupply()` | view | Umumi token supply |

**Events:**
```solidity
event Transfer(address indexed from, address indexed to, uint256 value);
event Approval(address indexed owner, address indexed spender, uint256 value);
```

**Gas Estimation:**
| Emeliyyat | Taxmini Gas |
|-----------|-------------|
| mint | ~51,000 |
| transfer | ~51,000 |
| approve | ~46,000 |
| transferFrom | ~60,000 |

---

### 2.2. EnergyTrading.sol (Esas muqavile)

**Meqsed:** P2P enerji ticareti. Prosumer elan yaradir, Consumer alir.

**State Variables:**
```solidity
IERC20 public energyToken;           // EnergyToken referansi
uint256 public offerCount;            // Elan saygaci
uint256 public platformFeePercent;    // Platform komissiyasi (default: 2%)
address public feeCollector;          // Komissiya yigan adres
mapping(uint256 => EnergyOffer) public offers;
mapping(address => uint256[]) public sellerOffers;   // Saticinin elanlari
mapping(address => uint256[]) public buyerPurchases; // Alicinin alislari
```

**Enums:**
```solidity
enum OfferStatus { Active, Sold, Cancelled, Expired, Settled }
enum EnergySource { Solar, Wind, Hydro, Mixed }
```

**Structs:**
```solidity
struct EnergyOffer {
    uint256 id;
    address seller;
    uint256 energyAmount;      // kWh * 1000 (3 decimal precision)
    uint256 pricePerUnit;      // wei per kWh
    uint256 totalPrice;        // energyAmount * pricePerUnit
    uint256 createdAt;
    uint256 expiresAt;
    EnergySource source;
    OfferStatus status;
    address buyer;             // address(0) eger satilmayib
    uint256 settledAt;         // settle timestamp
}
```

**Funksiyalar:**

| Funksiya | Gosterici | Tesvir |
|----------|-----------|--------|
| `createOffer(uint256 energyAmount, uint256 pricePerUnit, uint256 duration, EnergySource source)` | public | Yeni elan yaradir. `duration` saniye ile. |
| `buyEnergy(uint256 offerId)` | public | Alici offer alir. Token `transferFrom` ile kecir. Evvelce `approve` lazimdir. |
| `cancelOffer(uint256 offerId)` | public | Yalniz satici oz elanini legv ede biler. Status Active olmalidir. |
| `settleTransaction(uint256 offerId)` | onlyRole(OPERATOR_ROLE) | Grid operator enerji catdirildigini tesdiq edir. Token saticia kecir. |
| `getOffer(uint256 offerId)` | view | Tek elan detali |
| `getActiveOffers()` | view | Butun aktiv elanlar |
| `getSellerOffers(address seller)` | view | Saticinin butun elanlari |
| `getBuyerPurchases(address buyer)` | view | Alicinin butun alislari |
| `expireOffer(uint256 offerId)` | public | Mudeti kecmis elanin statusunu Expired edir |

**Events:**
```solidity
event OfferCreated(uint256 indexed offerId, address indexed seller, uint256 energyAmount, uint256 pricePerUnit, EnergySource source);
event EnergyPurchased(uint256 indexed offerId, address indexed buyer, address indexed seller, uint256 energyAmount, uint256 totalPrice);
event OfferCancelled(uint256 indexed offerId, address indexed seller);
event TransactionSettled(uint256 indexed offerId, address indexed buyer, address indexed seller, uint256 settledAt);
event OfferExpired(uint256 indexed offerId);
```

**Token axini (buyEnergy):**
```
1. Buyer evvelce EnergyToken.approve(TradingContract, totalPrice) cagirir
2. Buyer EnergyTrading.buyEnergy(offerId) cagirir
3. Contract EnergyToken.transferFrom(buyer, contract, totalPrice) edir
4. Offer statusu Sold olur, buyer yazilir
5. EnergyPurchased event emit olunur
6. Grid operator fiziki enerjini verify edir
7. Operator settleTransaction(offerId) cagirir
8. Contract EnergyToken.transfer(seller, totalPrice - fee) edir
9. Contract EnergyToken.transfer(feeCollector, fee) edir
10. TransactionSettled event emit olunur
```

**Gas Estimation:**
| Emeliyyat | Taxmini Gas |
|-----------|-------------|
| createOffer | ~120,000 |
| buyEnergy | ~150,000 |
| cancelOffer | ~45,000 |
| settleTransaction | ~80,000 |

---

### 2.3. SmartMeterRegistry.sol

**Meqsed:** Smart meter cihazlarinin on-chain qeydiyyati ve data hash-lerinin saxlanmasi.

**State Variables:**
```solidity
mapping(bytes32 => SmartMeter) public meters;
mapping(address => bytes32[]) public ownerMeters;
mapping(bytes32 => MeterReading[]) public meterReadings;
```

**Structs:**
```solidity
struct SmartMeter {
    bytes32 meterId;
    address owner;
    string location;          // encrypted/hashed location
    uint256 registeredAt;
    bool isActive;
}

struct MeterReading {
    uint256 timestamp;
    bytes32 dataHash;         // keccak256(consumption, production, voltage, frequency)
    uint256 consumptionWh;    // Wh (Watt-hour) daha deqiq
    uint256 productionWh;
}
```

**Funksiyalar:**

| Funksiya | Gosterici | Tesvir |
|----------|-----------|--------|
| `registerMeter(bytes32 meterId, string location)` | public | Yeni meter qeydiyyati. msg.sender owner olur. |
| `submitReading(bytes32 meterId, uint256 consumption, uint256 production, bytes32 dataHash)` | onlyMeterOwner | Data hash yazilir. Raw data IPFS/PostgreSQL-de. |
| `getLatestReading(bytes32 meterId)` | view | Son oxuma |
| `getMeterHistory(bytes32 meterId, uint256 from, uint256 to)` | view | Tarix araliginda oxumalar |
| `deactivateMeter(bytes32 meterId)` | onlyMeterOwner | Meteri deaktiv edir |
| `verifyReading(bytes32 meterId, uint256 timestamp, bytes32 dataHash)` | view | Data hash-i yoxlayir (tamper check) |

**Events:**
```solidity
event MeterRegistered(bytes32 indexed meterId, address indexed owner, uint256 registeredAt);
event ReadingSubmitted(bytes32 indexed meterId, uint256 timestamp, bytes32 dataHash, uint256 consumptionWh, uint256 productionWh);
event MeterDeactivated(bytes32 indexed meterId);
```

**Data integrity flow:**
```
1. Smart meter sensor data oxuyur (consumption, production, voltage, frequency)
2. Backend raw data-ni PostgreSQL/TimescaleDB-e yazir
3. Backend data-nin hash-ini hesablayir: keccak256(abi.encode(consumption, production, voltage, frequency))
4. Backend SmartMeterRegistry.submitReading() cagirir (hash on-chain yazilir)
5. Sonradan verify ucun: off-chain data-dan hash hesablanir ve on-chain hash ile muqayise olunur
```

**Gas Estimation:**
| Emeliyyat | Taxmini Gas |
|-----------|-------------|
| registerMeter | ~95,000 |
| submitReading | ~75,000 |

**Qeyd:** submitReading her deqiqe cagirilmamalidir. Optimal: her 1 saat ve ya her gun batch hash.

---

### 2.4. RewardDistributor.sol

**Meqsed:** Enerji qenayeti ve yasil enerji istifadesi ucun token mukafatlari.

**State Variables:**
```solidity
IERC20 public energyToken;
mapping(address => uint256) public totalRewardsEarned;
mapping(address => Reward[]) public rewardHistory;
uint256 public rewardPool;                              // Movcud reward pool
```

**Structs:**
```solidity
struct Reward {
    uint256 amount;
    string reason;
    uint256 timestamp;
}
```

**Reward qaydalari:**
```
1. Az istehlak bonusu:    gunluk istehlak < 10kWh → 5 SGET
2. Solar istehsal:        gunluk istehsal > 20kWh → 10 SGET
3. Pik saatdan kacma:     18:00-21:00 arasi istehlak < 2kWh → 3 SGET
4. Ardicil qenayet:       7 gun ardicil az istehlak → 25 SGET
5. Ilk ticarot bonusu:    ilk P2P alis/satis → 50 SGET
```

**Funksiyalar:**

| Funksiya | Gosterici | Tesvir |
|----------|-----------|--------|
| `distributeReward(address user, uint256 amount, string reason)` | onlyRole(DISTRIBUTOR_ROLE) | Token reward transfer |
| `fundRewardPool(uint256 amount)` | onlyRole(ADMIN_ROLE) | Reward pool-a token elave et |
| `getRewardHistory(address user)` | view | Istifadecinin reward tarixcesi |
| `getTotalRewards(address user)` | view | Umumi qaznailmis reward |
| `getRewardPoolBalance()` | view | Movcud pool balansi |

**Events:**
```solidity
event RewardDistributed(address indexed user, uint256 amount, string reason, uint256 timestamp);
event RewardPoolFunded(uint256 amount, uint256 newBalance);
```

**Gas Estimation:**
| Emeliyyat | Taxmini Gas |
|-----------|-------------|
| distributeReward | ~70,000 |
| fundRewardPool | ~55,000 |

---

### 2.5. GridGovernance.sol

**Meqsed:** Decentralized idare etme - qiymet deyisiklikleri, qayda yenilemeleri ucun sesverme.

**State Variables:**
```solidity
uint256 public proposalCount;
uint256 public votingPeriod;          // default: 7 gun (604800 saniye)
uint256 public quorumPercent;         // default: 30%
mapping(uint256 => Proposal) public proposals;
mapping(uint256 => mapping(address => bool)) public hasVoted;
```

**Structs:**
```solidity
struct Proposal {
    uint256 id;
    address proposer;
    string description;
    bytes data;                       // encoded function call (eger varsa)
    uint256 forVotes;
    uint256 againstVotes;
    uint256 createdAt;
    uint256 endsAt;
    bool executed;
    ProposalStatus status;
}

enum ProposalStatus { Active, Passed, Rejected, Executed }
```

**Funksiyalar:**

| Funksiya | Gosterici | Tesvir |
|----------|-----------|--------|
| `createProposal(string description, bytes data)` | public (min token balance lazim) | Yeni teklif yaradir |
| `vote(uint256 proposalId, bool support)` | public (token holder) | Ses verir. 1 adres = 1 ses. |
| `executeProposal(uint256 proposalId)` | public | Qebul olunmus teklifi icra edir. Voting period bitmeli ve quorum olmalidir. |
| `getProposal(uint256 proposalId)` | view | Teklif detali |
| `getActiveProposals()` | view | Aktiv teklifler |

**Events:**
```solidity
event ProposalCreated(uint256 indexed proposalId, address indexed proposer, string description);
event Voted(uint256 indexed proposalId, address indexed voter, bool support);
event ProposalExecuted(uint256 indexed proposalId);
```

---

## 3. Contract-lar arasi elaqe

```
                    ┌──────────────────┐
                    │  GridGovernance  │
                    │    .sol          │
                    └────────┬─────────┘
                             │ governs (fee %, rules)
                             ▼
┌──────────────┐    ┌──────────────────┐    ┌───────────────────┐
│ EnergyToken  │◄───│  EnergyTrading   │───►│ SmartMeterRegistry│
│   .sol       │    │     .sol         │    │      .sol         │
│              │    │                  │    │                   │
│ ERC-20 token │    │ P2P marketplace  │    │ Meter verification│
│ mint/burn    │    │ buy/sell/settle  │    │ data hash store   │
└──────┬───────┘    └────────┬─────────┘    └───────────────────┘
       │                     │
       │                     │ triggers reward
       │                     ▼
       │            ┌──────────────────┐
       └───────────►│ RewardDistributor│
                    │      .sol        │
                    │ reward payout    │
                    └──────────────────┘
```

**Deploy sirasi (dependency order):**
1. `EnergyToken.sol` - ilk deploy (diger contractlar buna referans edir)
2. `SmartMeterRegistry.sol` - asili deyil
3. `EnergyTrading.sol` - constructor-a EnergyToken address lazim
4. `RewardDistributor.sol` - constructor-a EnergyToken address lazim
5. `GridGovernance.sol` - butun contractlara referans ede biler

---

## 4. Ktor Backend ↔ Blockchain Inteqrasiya

### 4.1. Backend-in blockchain ile elaqesi

```
Ktor Backend (Web3j)
       │
       ├── READ (view functions) ───► Blockchain-den data oxu (gas yoxdur)
       │     balanceOf(), getOffer(), getActiveOffers()
       │
       ├── WRITE (transactions) ───► Blockchain-e data yaz (gas lazimdir)
       │     createOffer(), buyEnergy(), settleTransaction()
       │
       └── LISTEN (events) ────────► Contract event-lerini dinle
             OfferCreated, EnergyPurchased, TransactionSettled
             │
             └── PostgreSQL-i sync et (off-chain cache yenile)
```

### 4.2. Event Listener → PostgreSQL Sync

Backend contract event-lerini dinleyir ve PostgreSQL-i yenileyir:

```
Blockchain Event              →  PostgreSQL Action
─────────────────────────────────────────────────
OfferCreated                  →  INSERT INTO energy_offers
EnergyPurchased               →  UPDATE offers SET status='SOLD', INSERT INTO transactions
OfferCancelled                →  UPDATE offers SET status='CANCELLED'
TransactionSettled            →  UPDATE transactions SET status='SETTLED'
Transfer (ERC-20)             →  UPDATE wallet_cache SET balance=...
RewardDistributed             →  INSERT INTO rewards
MeterRegistered               →  INSERT INTO smart_meters (on-chain flag=true)
```

### 4.3. Wallet Management

| Ssenari | Yanaşma |
|---------|---------|
| Istifadecinin oz wallet-i var (MetaMask) | Frontend walletAddress-i backend-e gonderir, backend DB-ye yazir |
| Istifadecinin wallet-i yoxdur | Backend custodial wallet yaradir (Web3j), private key encrypted saxlanir |
| Transaction imzalama | Frontend imzalayir (MetaMask) ve ya backend custodial key ile imzalayir |

---

## 5. Sepolia Testnet Konfiqurasiya

```
Network: Sepolia
Chain ID: 11155111
RPC URL: https://sepolia.infura.io/v3/YOUR_PROJECT_ID
       ve ya https://eth-sepolia.g.alchemy.com/v2/YOUR_API_KEY
Block Explorer: https://sepolia.etherscan.io
Faucet: https://sepoliafaucet.com
```

**Lazim olan servisler:**
- **Infura** ve ya **Alchemy** - RPC provider (pulsuz tier kifayetdir)
- **Sepolia ETH** - faucet-den test ETH al (gas ucun)
- **Etherscan API key** - contract verify ucun

---

## 6. Gas Optimizasiya Strategiyalari

| Strategiya | Tesvir |
|-----------|--------|
| Batch reading submission | Meter data-ni her saat ve ya gun batch hash kimi gondor, her deqiqe yox |
| Event-based caching | On-chain data-ni her defe oxuma, event-lerle PostgreSQL-i sync et |
| Minimal on-chain storage | Yalniz hash saxla, raw data off-chain |
| Pack structs | Solidity struct-larda uint256 yerine uint128/uint64 istifade et (gas azaldir) |
| View functions ucun off-chain fallback | getActiveOffers() yerine PostgreSQL-den oxu (daha suretli, gas yoxdur) |

---

## 7. Security Checklist

- [ ] Reentrancy guard (ReentrancyGuard) butun write funksiyalarda
- [ ] Access control (MINTER_ROLE, OPERATOR_ROLE, ADMIN_ROLE)
- [ ] Integer overflow protection (Solidity 0.8+ default)
- [ ] Offer expiration check (block.timestamp)
- [ ] Double-buy prevention (status check)
- [ ] Seller !== buyer check
- [ ] Zero address check
- [ ] Approve before transferFrom (front-running mitigation)
- [ ] Emergency pause (Pausable)
- [ ] Upgradeable proxy pattern (istege bagli)

---

## 8. Test Ssenari Checklist

### EnergyToken
- [ ] Mint yalniz MINTER_ROLE ile isleyir
- [ ] Transfer dogru isleyir
- [ ] Approve + transferFrom dogru isleyir
- [ ] MAX_SUPPLY limit yoxlanir
- [ ] Burn dogru isleyir

### EnergyTrading
- [ ] createOffer dogru struct yaradir
- [ ] buyEnergy token transfer edir
- [ ] buyEnergy yalniz Active offer-de isleyir
- [ ] cancelOffer yalniz seller ucun isleyir
- [ ] settleTransaction yalniz OPERATOR ile isleyir
- [ ] Expired offer ala bilmir
- [ ] Eyni offer-i 2 defe ala bilmir
- [ ] Platform fee dogru hesablanir

### SmartMeterRegistry
- [ ] registerMeter dogru isleyir
- [ ] submitReading yalniz owner ile isleyir
- [ ] verifyReading hash muqayisesi dogrudur
- [ ] Deaktiv meter-e reading submit olmur

### RewardDistributor
- [ ] distributeReward token transfer edir
- [ ] Reward pool-dan cixir
- [ ] Pool bitdikde revert edir
- [ ] Tarixce dogru saxlanir

### GridGovernance
- [ ] Proposal yaratmaq ucun min token balance lazimdir
- [ ] 1 adres 1 defe ses vere biler
- [ ] Voting period bitdikden sonra execute olunur
- [ ] Quorum yoxlanir
