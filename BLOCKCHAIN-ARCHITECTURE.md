# SmartGrid Blockchain & Smart Contract Architecture

**Network:** Ethereum (Sepolia Testnet → Mainnet)
**Language:** Solidity ^0.8.20
**Framework:** Hardhat
**Token Standard:** ERC-20 (OpenZeppelin)
**Backend Integration:** Web3j (Ktor)

---

## 1. On-chain vs Off-chain Data Strategy

### ON-CHAIN (Stored on Blockchain)

| Data | Contract | Rationale |
|------|----------|-----------|
| EnergyToken balances | EnergyToken.sol | Decentralized ownership, ERC-20 standard compliance |
| Token transfers (mint/burn/transfer) | EnergyToken.sol | Transparency, immutable audit trail |
| Energy offers (listings) | EnergyTrading.sol | Trustless marketplace, double-sale prevention |
| Energy purchase transactions | EnergyTrading.sol | Immutability, transparent settlement |
| Offer status (Active/Sold/Cancelled) | EnergyTrading.sol | On-chain state tracking |
| Smart meter registration | SmartMeterRegistry.sol | Tamper-proof device registry |
| Meter data hashes | SmartMeterRegistry.sol | Verification (raw data stored off-chain) |
| Reward distribution records | RewardDistributor.sol | Transparent tokenomics, auditable payouts |
| Governance proposals and voting | GridGovernance.sol | Decentralized decision-making |

### OFF-CHAIN (Stored in PostgreSQL/Redis)

| Data | Storage | Rationale |
|------|---------|-----------|
| User profiles (email, name, password) | PostgreSQL | Privacy, GDPR compliance, frequently updated |
| JWT/Session tokens | Redis | Ephemeral data, fast access required |
| Raw smart meter data | PostgreSQL + TimescaleDB | High volume, gas cost prohibitive on-chain |
| Real-time streaming data | Redis + WebSocket | Low-latency requirement |
| Analytics/Reports (aggregated) | TimescaleDB | Query performance optimization |
| Notification history | PostgreSQL | Not blockchain-relevant |
| IoT device configuration | PostgreSQL | Frequently updated |

### IPFS (Decentralized File Storage)

| Data | Rationale |
|------|-----------|
| Meter data raw batches | Large files, too expensive for blockchain storage |
| PDF/CSV exported reports | Exported documents |
| Contract metadata | ABI definitions, deployment info |

---

## 2. Smart Contracts

### 2.1. EnergyToken.sol (ERC-20)

**Purpose:** The platform's energy token used for energy trading and reward distribution.

**OpenZeppelin Extensions:** ERC20, ERC20Burnable, Ownable, AccessControl

**Roles:**
- `MINTER_ROLE` — can mint new tokens (Grid Operator)
- `DEFAULT_ADMIN_ROLE` — manages roles (Admin)

**State Variables:**
```solidity
string public constant NAME = "SmartGrid Energy Token";
string public constant SYMBOL = "SGET";
uint8 public constant DECIMALS = 18;
uint256 public constant MAX_SUPPLY = 1_000_000_000 * 10**18; // 1 billion
```

**Functions:**

| Function | Access | Description |
|----------|--------|-------------|
| `mint(address to, uint256 amount)` | MINTER_ROLE | Mint new tokens to an address |
| `burn(uint256 amount)` | public | Burn caller's own tokens |
| `transfer(address to, uint256 amount)` | public | Transfer tokens to another address |
| `approve(address spender, uint256 amount)` | public | Approve marketplace spending allowance |
| `transferFrom(address from, address to, uint256 amount)` | public | Transfer from approved allowance |
| `balanceOf(address account)` | view | Query token balance |
| `totalSupply()` | view | Query total token supply |

**Events:**
```solidity
event Transfer(address indexed from, address indexed to, uint256 value);
event Approval(address indexed owner, address indexed spender, uint256 value);
```

**Gas Estimates:**

| Operation | Estimated Gas |
|-----------|---------------|
| mint | ~51,000 |
| transfer | ~51,000 |
| approve | ~46,000 |
| transferFrom | ~60,000 |

---

### 2.2. EnergyTrading.sol (Core Marketplace)

**Purpose:** Peer-to-peer energy trading. Prosumers create offers, Consumers purchase energy.

**State Variables:**
```solidity
IERC20 public energyToken;           // EnergyToken reference
uint256 public offerCount;            // Offer counter
uint256 public platformFeePercent;    // Platform commission (default: 2%)
address public feeCollector;          // Commission collector address
mapping(uint256 => EnergyOffer) public offers;
mapping(address => uint256[]) public sellerOffers;   // Seller's offers
mapping(address => uint256[]) public buyerPurchases; // Buyer's purchases
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
    address buyer;             // address(0) if unsold
    uint256 settledAt;         // settlement timestamp
}
```

**Functions:**

| Function | Access | Description |
|----------|--------|-------------|
| `createOffer(uint256 energyAmount, uint256 pricePerUnit, uint256 duration, EnergySource source)` | public | Create a new energy offer. `duration` is in seconds. |
| `buyEnergy(uint256 offerId)` | public | Purchase energy from an offer. Requires prior `approve()` call on EnergyToken. |
| `cancelOffer(uint256 offerId)` | public | Cancel own offer. Only the seller can cancel. Status must be Active. |
| `settleTransaction(uint256 offerId)` | OPERATOR_ROLE | Grid operator confirms energy delivery. Transfers tokens to seller. |
| `getOffer(uint256 offerId)` | view | Get single offer details |
| `getActiveOffers()` | view | Get all active offers |
| `getSellerOffers(address seller)` | view | Get all offers by a seller |
| `getBuyerPurchases(address buyer)` | view | Get all purchases by a buyer |
| `expireOffer(uint256 offerId)` | public | Mark expired offer as Expired |

**Events:**
```solidity
event OfferCreated(uint256 indexed offerId, address indexed seller, uint256 energyAmount, uint256 pricePerUnit, EnergySource source);
event EnergyPurchased(uint256 indexed offerId, address indexed buyer, address indexed seller, uint256 energyAmount, uint256 totalPrice);
event OfferCancelled(uint256 indexed offerId, address indexed seller);
event TransactionSettled(uint256 indexed offerId, address indexed buyer, address indexed seller, uint256 settledAt);
event OfferExpired(uint256 indexed offerId);
```

**Token Flow (buyEnergy):**
```
 1. Buyer calls EnergyToken.approve(TradingContract, totalPrice)
 2. Buyer calls EnergyTrading.buyEnergy(offerId)
 3. Contract calls EnergyToken.transferFrom(buyer, contract, totalPrice)
 4. Offer status changes to Sold, buyer address recorded
 5. EnergyPurchased event emitted
 6. Grid operator verifies physical energy delivery
 7. Operator calls settleTransaction(offerId)
 8. Contract calls EnergyToken.transfer(seller, totalPrice - fee)
 9. Contract calls EnergyToken.transfer(feeCollector, fee)
10. TransactionSettled event emitted
```

**Gas Estimates:**

| Operation | Estimated Gas |
|-----------|---------------|
| createOffer | ~120,000 |
| buyEnergy | ~150,000 |
| cancelOffer | ~45,000 |
| settleTransaction | ~80,000 |

---

### 2.3. SmartMeterRegistry.sol

**Purpose:** On-chain registration of smart meter devices and storage of data hashes for integrity verification.

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
    uint256 consumptionWh;    // Wh (Watt-hour) for precision
    uint256 productionWh;
}
```

**Functions:**

| Function | Access | Description |
|----------|--------|-------------|
| `registerMeter(bytes32 meterId, string location)` | public | Register a new meter. `msg.sender` becomes the owner. |
| `submitReading(bytes32 meterId, uint256 consumption, uint256 production, bytes32 dataHash)` | onlyMeterOwner | Submit a data hash. Raw data stored in PostgreSQL/IPFS. |
| `getLatestReading(bytes32 meterId)` | view | Get the most recent reading |
| `getMeterHistory(bytes32 meterId, uint256 from, uint256 to)` | view | Get readings within a time range |
| `deactivateMeter(bytes32 meterId)` | onlyMeterOwner | Deactivate a meter |
| `verifyReading(bytes32 meterId, uint256 timestamp, bytes32 dataHash)` | view | Verify data hash integrity (tamper check) |

**Events:**
```solidity
event MeterRegistered(bytes32 indexed meterId, address indexed owner, uint256 registeredAt);
event ReadingSubmitted(bytes32 indexed meterId, uint256 timestamp, bytes32 dataHash, uint256 consumptionWh, uint256 productionWh);
event MeterDeactivated(bytes32 indexed meterId);
```

**Data Integrity Flow:**
```
1. Smart meter sensor reads data (consumption, production, voltage, frequency)
2. Backend writes raw data to PostgreSQL/TimescaleDB
3. Backend computes hash: keccak256(abi.encode(consumption, production, voltage, frequency))
4. Backend calls SmartMeterRegistry.submitReading() (hash stored on-chain)
5. For verification: compute hash from off-chain data and compare with on-chain hash
```

**Gas Estimates:**

| Operation | Estimated Gas |
|-----------|---------------|
| registerMeter | ~95,000 |
| submitReading | ~75,000 |

> **Note:** `submitReading` should not be called every minute. Optimal frequency: hourly or daily batch hash submissions.

---

### 2.4. RewardDistributor.sol

**Purpose:** Distribute token rewards for energy savings and green energy usage.

**State Variables:**
```solidity
IERC20 public energyToken;
mapping(address => uint256) public totalRewardsEarned;
mapping(address => Reward[]) public rewardHistory;
uint256 public rewardPool;                              // Available reward pool
```

**Structs:**
```solidity
struct Reward {
    uint256 amount;
    string reason;
    uint256 timestamp;
}
```

**Reward Rules:**
```
1. Low consumption bonus:     daily consumption < 10kWh → 5 SGET
2. Solar production bonus:    daily production > 20kWh → 10 SGET
3. Peak avoidance:            consumption < 2kWh during 18:00-21:00 → 3 SGET
4. Consecutive savings:       7 consecutive low-consumption days → 25 SGET
5. First trade bonus:         first P2P buy/sell → 50 SGET
```

**Functions:**

| Function | Access | Description |
|----------|--------|-------------|
| `distributeReward(address user, uint256 amount, string reason)` | DISTRIBUTOR_ROLE | Transfer token reward to user |
| `fundRewardPool(uint256 amount)` | ADMIN_ROLE | Add tokens to the reward pool |
| `getRewardHistory(address user)` | view | Get user's reward history |
| `getTotalRewards(address user)` | view | Get total rewards earned by user |
| `getRewardPoolBalance()` | view | Get current pool balance |

**Events:**
```solidity
event RewardDistributed(address indexed user, uint256 amount, string reason, uint256 timestamp);
event RewardPoolFunded(uint256 amount, uint256 newBalance);
```

**Gas Estimates:**

| Operation | Estimated Gas |
|-----------|---------------|
| distributeReward | ~70,000 |
| fundRewardPool | ~55,000 |

---

### 2.5. GridGovernance.sol

**Purpose:** Decentralized governance — voting on price changes, rule updates, and platform parameters.

**State Variables:**
```solidity
uint256 public proposalCount;
uint256 public votingPeriod;          // default: 7 days (604,800 seconds)
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
    bytes data;                       // encoded function call (if applicable)
    uint256 forVotes;
    uint256 againstVotes;
    uint256 createdAt;
    uint256 endsAt;
    bool executed;
    ProposalStatus status;
}

enum ProposalStatus { Active, Passed, Rejected, Executed }
```

**Functions:**

| Function | Access | Description |
|----------|--------|-------------|
| `createProposal(string description, bytes data)` | public (min token balance required) | Create a new governance proposal |
| `vote(uint256 proposalId, bool support)` | public (token holder) | Cast a vote. 1 address = 1 vote. |
| `executeProposal(uint256 proposalId)` | public | Execute a passed proposal. Voting period must have ended with quorum reached. |
| `getProposal(uint256 proposalId)` | view | Get proposal details |
| `getActiveProposals()` | view | Get all active proposals |

**Events:**
```solidity
event ProposalCreated(uint256 indexed proposalId, address indexed proposer, string description);
event Voted(uint256 indexed proposalId, address indexed voter, bool support);
event ProposalExecuted(uint256 indexed proposalId);
```

---

## 3. Inter-Contract Relationships

```
                    ┌──────────────────┐
                    │  GridGovernance   │
                    │      .sol        │
                    └────────┬─────────┘
                             │ governs (fee %, rules)
                             ▼
┌──────────────┐    ┌──────────────────┐    ┌───────────────────┐
│ EnergyToken  │◄───│  EnergyTrading   │───►│ SmartMeterRegistry│
│    .sol      │    │      .sol        │    │       .sol        │
│              │    │                  │    │                   │
│ ERC-20 token │    │ P2P marketplace  │    │ Meter verification│
│ mint/burn    │    │ buy/sell/settle  │    │ data hash store   │
└──────┬───────┘    └────────┬─────────┘    └───────────────────┘
       │                     │
       │                     │ triggers reward
       │                     ▼
       │            ┌──────────────────┐
       └───────────►│ RewardDistributor│
                    │       .sol       │
                    │  reward payout   │
                    └──────────────────┘
```

**Deployment Order (dependency graph):**
1. `EnergyToken.sol` — deployed first (all other contracts reference it)
2. `SmartMeterRegistry.sol` — no dependencies
3. `EnergyTrading.sol` — constructor requires EnergyToken address
4. `RewardDistributor.sol` — constructor requires EnergyToken address
5. `GridGovernance.sol` — can reference all contracts

---

## 4. Ktor Backend ↔ Blockchain Integration

### 4.1. Backend Communication with Blockchain

```
Ktor Backend (Web3j)
       │
       ├── READ (view functions) ───► Read data from blockchain (no gas required)
       │     balanceOf(), getOffer(), getActiveOffers()
       │
       ├── WRITE (transactions) ───► Write data to blockchain (gas required)
       │     createOffer(), buyEnergy(), settleTransaction()
       │
       └── LISTEN (events) ────────► Listen for contract events
             OfferCreated, EnergyPurchased, TransactionSettled
             │
             └── Sync PostgreSQL (update off-chain cache)
```

### 4.2. Event Listener → PostgreSQL Synchronization

The backend listens for blockchain contract events and keeps PostgreSQL in sync:

```
Blockchain Event              →  PostgreSQL Action
─────────────────────────────────────────────────────
OfferCreated                  →  INSERT INTO energy_offers
EnergyPurchased               →  UPDATE offers SET status='SOLD', INSERT INTO transactions
OfferCancelled                →  UPDATE offers SET status='CANCELLED'
TransactionSettled            →  UPDATE transactions SET status='SETTLED'
Transfer (ERC-20)             →  UPDATE wallet_cache SET balance=...
RewardDistributed             →  INSERT INTO rewards
MeterRegistered               →  INSERT INTO smart_meters (on_chain_flag=true)
```

### 4.3. Wallet Management

| Scenario | Approach |
|----------|----------|
| User has own wallet (MetaMask) | Frontend sends walletAddress to backend; backend stores in DB |
| User has no wallet | Backend creates custodial wallet (Web3j); private key stored encrypted |
| Transaction signing | Frontend signs (MetaMask) or backend signs with custodial key |

---

## 5. Sepolia Testnet Configuration

```
Network:        Sepolia
Chain ID:       11155111
RPC URL:        https://sepolia.infura.io/v3/YOUR_PROJECT_ID
                or https://eth-sepolia.g.alchemy.com/v2/YOUR_API_KEY
Block Explorer: https://sepolia.etherscan.io
Faucet:         https://sepoliafaucet.com
```

**Required Services:**
- **Infura** or **Alchemy** — RPC provider (free tier is sufficient)
- **Sepolia ETH** — obtain test ETH from faucet (needed for gas)
- **Etherscan API key** — for contract verification

---

## 6. Gas Optimization Strategies

| Strategy | Description |
|----------|-------------|
| Batch reading submission | Submit meter data as hourly/daily batch hashes, not per-minute |
| Event-based caching | Avoid reading on-chain data repeatedly; sync via events to PostgreSQL |
| Minimal on-chain storage | Store only hashes on-chain, keep raw data off-chain |
| Pack structs | Use `uint128`/`uint64` instead of `uint256` in Solidity structs to reduce gas |
| Off-chain fallback for view functions | Read from PostgreSQL instead of calling `getActiveOffers()` on-chain (faster, no gas) |

---

## 7. Security Checklist

- [ ] Reentrancy guard (`ReentrancyGuard`) on all state-changing functions
- [ ] Access control (`MINTER_ROLE`, `OPERATOR_ROLE`, `ADMIN_ROLE`)
- [ ] Integer overflow protection (default in Solidity 0.8+)
- [ ] Offer expiration check (`block.timestamp`)
- [ ] Double-buy prevention (status validation)
- [ ] Seller ≠ buyer validation
- [ ] Zero address checks
- [ ] Approve before `transferFrom` (front-running mitigation)
- [ ] Emergency pause functionality (`Pausable`)
- [ ] Upgradeable proxy pattern (optional, for future upgrades)

---

## 8. Test Scenario Checklist

### EnergyToken
- [ ] Mint only works with `MINTER_ROLE`
- [ ] Transfer works correctly between addresses
- [ ] Approve + transferFrom works correctly
- [ ] `MAX_SUPPLY` limit is enforced
- [ ] Burn works correctly

### EnergyTrading
- [ ] `createOffer` produces correct struct
- [ ] `buyEnergy` transfers tokens correctly
- [ ] `buyEnergy` only works on Active offers
- [ ] `cancelOffer` only works for the offer seller
- [ ] `settleTransaction` only works with `OPERATOR_ROLE`
- [ ] Cannot buy an expired offer
- [ ] Cannot buy the same offer twice
- [ ] Platform fee is calculated correctly

### SmartMeterRegistry
- [ ] `registerMeter` works correctly
- [ ] `submitReading` only works for meter owner
- [ ] `verifyReading` hash comparison is correct
- [ ] Cannot submit reading for deactivated meter

### RewardDistributor
- [ ] `distributeReward` transfers tokens correctly
- [ ] Rewards are deducted from the pool
- [ ] Reverts when pool is depleted
- [ ] History is stored correctly

### GridGovernance
- [ ] Minimum token balance required to create proposal
- [ ] One address can vote only once per proposal
- [ ] Execution only after voting period ends
- [ ] Quorum validation is enforced
