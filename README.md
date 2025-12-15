generative-ai — How to build & run

This small Java Swing project visualizes a toy genetic algorithm. The app was developed and tested for Java 21. The instructions below show quick ways to compile and run on macOS (zsh). Adjust paths if your setup differs.

## Prerequisites
- Java 21 JDK installed (Temurin, Azul, Liberica, etc.).
- `javac`/`java` on PATH or use the full JDK paths.
- (Optional) Maven if you prefer `mvn package` builds.

You can install Temurin 21 on macOS with Homebrew:

```bash
brew install --cask temurin21
```

## Quick start (no Maven)
1. Open a terminal and change to the project root.

```bash
cd /path/to/generative-ai
```

2. Compile sources (targets Java 21):

```bash
javac --release 21 -d target/classes $(find src/main/java -name '*.java')
```

3. Run the app (use Java 21 binary if you have multiple JDKs):

```bash
java -cp target/classes App
# or explicitly (example path used by VS Code extension):
# /Users/<you>/.vscode/extensions/redhat.java-*/jre/21*/bin/java -cp target/classes App
```

The application opens a Swing window with controls, a population grid and a fitness history plot.

## Using Maven (optional)
If you use Maven, ensure `maven-compiler-plugin` targets Java 21. Then run:

```bash
mvn -DskipTests package
java -cp target/classes App
```

If the build fails with a class version error, recompile with `--release 21` or run the app with a Java 21 JRE.

## Reproducible runs
If you need reproducible experiments, seed the RNGs in `ga.GeneticAlgorithm` and `ga.Individual` (replace `new Random()` with `new Random(seed)`).

## Troubleshooting
- UnsupportedClassVersionError: compile with `--release 21` or run with Java 21.
- UI NullPointerException on start (Metal LAF issue): use the Basic LAF fix in `gui.ControlPanel` (already applied in this repo).
- If the GUI freezes, the GA may be doing heavy work on the EDT. Try smaller population/chromosome sizes or refactor compute off the EDT.

## Useful commands summary
```bash
# compile
javac --release 21 -d target/classes $(find src/main/java -name '*.java')
# run
java -cp target/classes App
```

If you want me to add a Maven `pom.xml` change, an executable jar or a small run script (`scripts/run.sh`), tell me which and I will add it.

---
Generated on December 14, 2025 — use Java 21 to run.
