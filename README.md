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

## How to Compile and Run

### Option 1: Command Line (Terminal / CMD / PowerShell)
Compile all Java files:
```bash
javac -d bin src/com/flood/*.java
```

Run the application:
```bash
java -cp bin com.flood.Main
```

### Option 2: Run in IDE (VS Code / IntelliJ / Eclipse)
1. Open the project folder in your IDE.
2. Navigate to `src/com/flood/Main.java`.
3. Click **Run** or press `Shift + F10` (IntelliJ) / `F5` (VS Code).
