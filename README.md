# Urban Flood Nowcasting System (Drainage and Rainfall Coupling)

An urban flood monitoring and short-term forecasting (nowcasting) system designed in Java.

## Day 1: Project Setup and Rainfall Module
- Basic project structure and architecture setup.
- Core `Rainfall` entity representing sensor observation (amount, duration, intensity, location).
- `RainfallManager` service to store, manage, and display records.
- Interactive console-based menu for real-time data entry and inspection.
- Formula-based intensity classification (Light, Moderate, Heavy, Torrential) without external ML libraries.

## How to Compile and Run

### Option 1: Command Line (Terminal / CMD / PowerShell)
Compile the Java files:
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
