# Genetic Algorithm Visualizer

This project is a Java-based GUI tool for visualizing the operation of a genetic algorithm. It features a modern dark mode interface and clear visual cues for genetic operations.

## Features
- **Population Visualization:** See each individual's chromosome as a row of colored squares.
- **Fitness Tracking:** View average and best fitness over time.
- **Control Panel:** Start, stop, step, and restart the algorithm; adjust mutation and crossover rates.
- **Dark Mode:** All panels use a clean, modern dark theme.
- **Allman Braces:** All Java code uses next-line (Allman-style) braces for clarity.

## Visual Guide
### Population Panel
- **Rows:** Each row is an individual in the population.
- **Squares:** Each square is a gene (bit) in the chromosome.
  - **Red squares:** Genes just mutated in the last generation.
  - **Green squares:** Genes just involved in crossover.
  - **Blue-toned squares:** Genes that are "on" (true); darker blue means higher fitness.
  - **Gray squares:** Genes that are "off" (false) and not recently mutated/crossed over.
- **Yellow outline:** Highlights the individual(s) with the best fitness.

### Stats Panel
Shows:
- Current generation
- Average fitness
- Best fitness

### Fitness History Panel
- Line chart of average fitness over generations.
- Blue line on a dark background.

### Control Panel
- Buttons to start, stop, step, and restart the algorithm.
- Sliders to adjust mutation and crossover rates.

## How to Run
1. Compile all Java files:
   ```powershell
   cd generative-ai
   javac -d out (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })
   ```
2. Run the app:
   ```powershell
   java -cp out App
   ```

## Reverting Dark Mode/Style Changes
All dark mode and style changes are marked in code with:
```java
// DARK MODE PATCH START
...changed code...
// DARK MODE PATCH END
```
To revert, remove the code between these comments in each affected file.

## License
MIT

| Key | Action               |
| --- | -------------------- |
| `S` | Start GA             |
| `P` | Stop/pause GA        |
| `N` | Step 1 generation    |
| `R` | Restart GA           |
| `D` | Randomize parameters |
