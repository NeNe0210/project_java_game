
---

## Week 2 — Two Encounters, One Evolving Player

### Compile

From the `student-starter` folder:

```bash
mkdir -p build
javac -d build src/main/java/edu/starter/*.java
```

### Run the game

```bash
java -cp build edu.starter.Main
```

The adventure has two battles in a row: a 30-HP enemy, then a new 45-HP enemy. The same player continues with the exact HP and resource left after the first battle. Losing the first battle ends the program.

Controls: `1` Attack (10 dmg) · `2` Defend (blocks 5 from next hit) · `3` Special (costs 3 resource, 15 dmg) · `4` Recover (+3 resource, max 10)

### Run the automated tests

```bash
java -cp build edu.starter.Main test
```

Expected: 11 lines ending in `passed` (Week 1 regression + T1–T5).

### Reproduce the reference example

Run the game and enter: `3`, `1`, `1`

```
=== Encounter 1 ===
Player HP: 100 | Resource: 5 | Enemy HP: 30
3
Player HP: 90 | Resource: 2 | Enemy HP: 15
1
Player HP: 80 | Resource: 2 | Enemy HP: 5
1
You have killed the enemy
=== Encounter 2 ===
Player HP: 80 | Resource: 2 | Enemy HP: 45
```

### Code structure (`Main.java`)

- `Combatant`: one fighter (player or enemy). Private HP, resource and defense, with methods that keep them within legal limits.
- `playBattle(player, enemy, in)`: runs one encounter and returns `true` if the player won.
- `main`: creates the player once, runs Encounter 1 and Encounter 2, clears defense between them, and prints the final result.


---


### Week 3

JDK 25

Same commands as week 2:
```
javac -d build src/main/java/edu/starter/*.java
java -cp build edu.starter.Main
java -cp build edu.starter.Main test
```

After winning the first battle you choose a reward, 1 for Potion (+25 hp, max 100) or 2 for X Attack (attack 10 to 12).
Wrong input asks again. If you lose the first battle you dont get a reward.
I moved the tests to Tests.java.