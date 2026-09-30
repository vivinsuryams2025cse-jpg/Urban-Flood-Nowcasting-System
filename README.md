# Urban Flood Nowcasting System (Drainage and Rainfall Coupling)

An urban flood monitoring and short-term forecasting (nowcasting) system designed in Java.

---

## Day 1: Project Setup and Rainfall Module
- Basic project structure and architecture setup.
- Core `Rainfall` entity representing sensor observations (amount, duration, intensity, location).
- `RainfallManager` service to store, manage, and display records.
- Interactive console-based menu for real-time data entry and inspection.
- Formula-based intensity classification (Light, Moderate, Heavy, Torrential) without external ML libraries.

---

## Day 2: Drainage System & Rainfall-Drainage Coupling Module
- Core `Drainage` entity storing:
  - Drainage ID (e.g., `DRN-101`)
  - Location name (e.g., `Sector 4 Central Canal`)
  - Maximum holding/flow capacity (in mm)
  - Current water level (in mm)
  - Operational status (`NORMAL`, `WARNING`, `OVERFLOW RISK`)
- `DrainageManager` service to store, look up, and manage drainage records.
- **Coupling Algorithm (Can Drainage Handle Rainfall?)**:
  - $\text{Total Projected Water Load} = \text{Current Water Level} + \text{Rainfall Amount}$
  - If $\text{Total Water Load} \le \text{Capacity} \implies$ **CAN HANDLE** (`YES`)
  - If $\text{Total Water Load} > \text{Capacity} \implies$ **OVERFLOW RISK** (`NO`)
- **Status Evaluation Logic**:
  - Load $< 60\%$ of Capacity $\implies$ `NORMAL`
  - $60\% \le \text{Load} \le 100\%$ of Capacity $\implies$ `WARNING`
  - Load $> 100\%$ of Capacity $\implies$ `OVERFLOW RISK`
- Automated city-wide coupling linking Day 1 rainfall sensor observations with Day 2 drainage units by location.

---

## Day 3: Flood Risk Calculation & Warning Module
- Core `FloodRisk` entity combining 4 critical hydrological parameters:
  1. **Rainfall Intensity** ($I$ in mm/hr)
  2. **Rainfall Duration** ($T$ in hours)
  3. **Drainage Capacity** ($C$ in mm)
  4. **Current Water Level** ($W$ in mm)
- **Coupling & Load Formulation**:
  - $\text{Incoming Rain Volume} = I \times T$
  - $\text{Total Water Load} = W + (I \times T)$
  - $\text{Capacity Utilization} = \left(\frac{\text{Total Water Load}}{C}\right) \times 100\%$
- **Rule-Based 3-Level Risk Classification** (No AI / ML required):
  - **`HIGH` Risk**:
    - Condition A: $\text{Total Water Load} > \text{Drainage Capacity}$ (Overflow active)
    - Condition B: $\text{Capacity Utilization} \ge 85\%$ **AND** Rainfall Intensity $\ge 15.0\text{ mm/hr}$ (Flash flood surge)
    - Warning: `RED ALERT` — Severe flooding underway/imminent, emergency response & evacuation.
  - **`MEDIUM` Risk**:
    - Condition A: $65\% \le \text{Capacity Utilization} \le 100\%$ (Elevated water load)
    - Condition B: Rainfall Intensity $\ge 20.0\text{ mm/hr}$ for Duration $\ge 1.5\text{ hours}$ (Prolonged storm stress)
    - Warning: `AMBER ADVISORY` — Street waterlogging expected in underpasses and low zones.
  - **`LOW` Risk**:
    - Condition: $\text{Capacity Utilization} < 65\%$ (Adequate headroom margin)
    - Warning: `GREEN STATUS` — Safe conditions, normal drainage operation.
- **Explainability**: Every risk assessment outputs the precise physical reason and an actionable warning alert.
- Seamless connection with Day 1 `Rainfall` and Day 2 `Drainage` models.
- City-wide flood risk batch evaluation across all coupled urban zones.
- Interactive custom 4-variable flood risk calculator.

---

## Day 4: Evacuation & Emergency Response Advisory Module
- Core `EvacuationAdvisory` entity coupling directly with the Day 3 `FloodRisk` object.
- **4-Tier Threat Classification**:
  - `CRITICAL` → Mandatory Evacuation (maps from HIGH risk)
  - `ELEVATED` → Recommended Evacuation (maps from MEDIUM risk)
  - `MODERATE` → Precautionary Advisory (LOW risk with ≥ 40% utilization)
  - `NORMAL`   → No Action Required (LOW risk, ample headroom)
- **Emergency Response Levels**: LEVEL-1 (Maximum) to LEVEL-4 (Routine)
- **Deterministic Shelter Assignment**: 5 predefined city shelters assigned per zone using a location-name hash — repeatable across runs.
- **Ordered Citizen Action Steps**: Context-specific, step-by-step directives tailored to each threat level.
- **Time-Based Flood Progression Nowcast** (linear accumulation model):
  - $\text{Projected Load}(t) = W + I \times t$ for $t \in \{0, 0.5, 1, 2, 3\}$ hours
  - Displays projected load, utilization %, and status at each time horizon.
- **Emergency Contact Escalation**: 5 official contacts (Flood Control Room, Fire & Rescue, Medical, Police, NDRF).
- City-wide evacuation advisory sweep with threat-level summary count across all zones.
- Interactive custom 4-parameter evacuation advisory calculator.

---

## Day 5: Flood Zone Classification Module
- Core `FloodZone` entity coupling:
  - Rainfall observations (Day 1)
  - Drainage channels and current water levels (Day 2)
  - Hydrological risk assessments (Day 3)
- **3-Tier Rule-Based Classification**:
  - `DANGER` (Red):
    - Conditions: Associated `HIGH` risk, total water load exceeding drainage capacity ($\text{Load} > C$), or capacity utilization $\ge 85\%$.
    - Status: Severe waterlogging and imminent or active overflow.
    - Precaution: Immediate evacuation or sheltering on upper floors; stay clear of low-lying roadways.
  - `WARNING` (Amber / Yellow):
    - Conditions: Associated `MEDIUM` risk, capacity utilization between $60\%$ and $85\%$, water level $\ge 60\%$ capacity, or high rainfall intensity ($\ge 15\text{ mm/hr}$).
    - Status: Drainage network under moderate-to-heavy pressure with localized water accumulation.
    - Precaution: Exercise caution, activate stormwater pumps, avoid parking in low zones.
  - `SAFE` (Green):
    - Conditions: Capacity utilization $< 60\%$, normal water level buffer, safe discharge flow.
    - Status: Normal hydrological conditions; adequate headroom available.
    - Precaution: Routine monitoring; no immediate flood threat.
- **Menu Integration**:
  - Added Option `12. Check Flood Zone` in `Main.java`.
  - Supports single drainage zone assessment, city-wide flood zone sweep (`ALL`), and custom manual simulation (`MANUAL`).
- Beginner-friendly, transparent rule-based implementation with no external ML or heavy frameworks.

---

---

## Professional Spring Boot Layered Architecture

The project has been fully restructured into a professional **Spring Boot 3.3.4** layered architecture.

### Technology Stack
- **Java 21** | **Spring Boot 3.3.4** | **Maven**
- **Spring Web** (REST APIs) | **Spring Data JPA** (Hibernate + MySQL) | **Spring Validation**

### Final Package Structure

```
src/main/java/com/flood/
├── UrbanFloodNowcastingApplication.java   ← @SpringBootApplication entry point
├── controller/    ← @RestController HTTP endpoints
├── dto/           ← Request/Response Data Transfer Objects (@Valid)
├── entity/        ← @Entity JPA database tables (Rainfall, Drainage, FloodRisk, FloodAlert, FloodZone)
├── repository/    ← @Repository Spring Data JPA (JpaRepository)
├── service/       ← @Service business logic + preserved Day 1-4 legacy classes
├── exception/     ← ResourceNotFoundException, GlobalExceptionHandler
└── config/        ← DatabaseConfig (JPA + Transaction Management)

src/main/resources/
└── application.properties   ← MySQL datasource, JPA/Hibernate config
```

### REST API Endpoints

| Method | URL | Feature |
|--------|-----|---------|
| `POST` | `/api/rainfall` | Add rainfall data |
| `GET`  | `/api/rainfall` | View all rainfall data |
| `GET`  | `/api/rainfall/heavy` | Heavy rainfall zones |
| `POST` | `/api/drainage` | Add drainage system |
| `GET`  | `/api/drainage` | View all drainage systems |
| `GET`  | `/api/drainage/{id}/water-level` | Monitor water level |
| `PUT`  | `/api/drainage/{id}/water-level?level=35` | Update water level |
| `GET`  | `/api/drainage/overflow-risk` | Drainage at overflow risk |
| `POST` | `/api/flood-risk/calculate/{drainageId}` | Calculate coupled flood risk |
| `POST` | `/api/flood-risk/calculate-custom` | Custom flood risk simulation |
| `GET`  | `/api/flood-risk/zone/{drainageId}` | Check flood zone (SAFE/WARNING/DANGER) |
| `GET`  | `/api/flood-risk/history` | View flood risk history |
| `POST` | `/api/alerts/generate/{drainageId}` | Generate emergency alert |
| `GET`  | `/api/alerts` | View all alerts |
| `GET`  | `/api/reports/city-wide` | City-wide flood report |
| `GET`  | `/api/reports/summary` | Threat level summary |

### How to Build and Run (Spring Boot)

**1. Configure MySQL credentials** in `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

**2. Build with Maven:**
```bash
mvn clean package -DskipTests
```

**3. Run the Spring Boot application:**
```bash
java -jar target/urban-flood-nowcasting-1.0.0.jar
```
Server starts at `http://localhost:8080`

### Legacy Console Mode (Days 1–5)
The original console-based `Main.java` (Days 1–5 logic) has been preserved inside the service layer:
- `service/RainfallManager.java` — Day 1 in-memory rainfall manager
- `service/DrainageManager.java` — Day 2 in-memory drainage manager
- `service/EvacuationAdvisory.java` — Day 4 evacuation advisory engine

