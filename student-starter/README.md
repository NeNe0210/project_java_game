# Java & OOP Adventure — Student Starter

**Start with your own design.** The files here deliberately do not contain a game engine, combatant hierarchy, future rules, or an instructor solution.

Prerequisites: JDK 25, Maven, Git, terminal/IDE.

```bash
java -version
mvn test
mvn exec:java -Dexec.mainClass=edu.starter.Main
```

If the Maven exec plugin is unavailable, compile the starter directly:

```bash
mkdir -p build
javac -d build src/main/java/edu/starter/*.java
java -cp build edu.starter.Main
```

Choose one theme: Fantasy Dungeon, Space Skirmish, Robot Arena or Creature Tournament.
Read the workshop PDF for the current week and maintain one evolving Git repository all semester.

Utilities: `ConsoleChoice.read()` for safe numeric input, `Checks.that()` and `Checks.within()` for early tests. There are no hidden future requirements in this kit.
