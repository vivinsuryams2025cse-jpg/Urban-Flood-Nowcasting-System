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
