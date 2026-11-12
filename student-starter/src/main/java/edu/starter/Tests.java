package edu.starter;     
import edu.starter.Main.Combatant;

final class Tests {

    private Tests() {}

           static void runTests() {
                // ======== Week 1 (regression) ========

                // [Week 1] W01-ATK-01: attack deals 10
                Combatant e1 = new Combatant("E", 30);
                e1.take_damage(10);
                Checks.that(e1.getHP() == 20, "W01-ATK-01 failed: enemy HP is " + e1.getHP());
                System.out.println("[Week 1] W01-ATK-01 passed");

                // [Week 1] W01-DEF-01: defend blocks 5 and clears
                Combatant p2 = new Combatant("P", 100);
                p2.defend();
                p2.take_damage(10);
                Checks.that(p2.getHP() == 95, "W01-DEF-01 failed: player HP is " + p2.getHP());
                Checks.that(!p2.isDefending(), "W01-DEF-01 failed: defense was not cleared");
                System.out.println("[Week 1] W01-DEF-01 passed");

                // [Week 1] W01-SPC-01: special costs 3, deals 15
                Combatant p3 = new Combatant("P", 100, 5);
                Combatant e3 = new Combatant("E", 30);
                Checks.that(p3.special(), "W01-SPC-01 failed: special was rejected");
                e3.take_damage(15);
                Checks.that(p3.getResource() == 2, "W01-SPC-01 failed: resource is " + p3.getResource());
                Checks.that(e3.getHP() == 15, "W01-SPC-01 failed: enemy HP is " + e3.getHP());
                System.out.println("[Week 1] W01-SPC-01 passed");

                // [Week 1] W01-RES-01: recover 9 -> 10, rejected at 10
                Combatant p4 = new Combatant("P", 100, 9);
                Checks.that(p4.recover(), "W01-RES-01 failed: recover was rejected");
                Checks.that(p4.getResource() == 10, "W01-RES-01 failed: resource is " + p4.getResource());
                Checks.that(!p4.recover(), "W01-RES-01 failed: recover at 10 was accepted");
                System.out.println("[Week 1] W01-RES-01 passed");

                // [Week 1] EDGE-SPC-3: special at exactly 3 leaves 0
                Combatant p5 = new Combatant("P", 100, 3);
                Checks.that(p5.special(), "EDGE-SPC-3 failed: special at 3 was rejected");
                Checks.that(p5.getResource() == 0, "EDGE-SPC-3 failed: resource is " + p5.getResource());
                System.out.println("[Week 1] EDGE-SPC-3 passed");

                // ======== Week 2 ========

                // [Week 2] T1 / W02-OBJ-01: two enemies are independent
                Combatant a = new Combatant("A", 30);
                Combatant b = new Combatant("B", 45);
                a.take_damage(10);
                Checks.that(a.getHP() == 20, "T1 failed: A is " + a.getHP());
                Checks.that(b.getHP() == 45, "T1 failed: B is " + b.getHP());
                System.out.println("[Week 2] T1 / W02-OBJ-01 passed");

                // [Week 2] T2 / W02-ENC-01: player state carries over (trace from page 2)
                Combatant player = new Combatant("P", 100);
                Combatant enemyA = new Combatant("A", 30);
                player.special(); enemyA.take_damage(15);
                player.take_damage(10);
                enemyA.take_damage(10);
                player.take_damage(10);
                enemyA.take_damage(10);
                Combatant enemyB = new Combatant("B", 45);
                Checks.that(player.getHP() == 80, "T2 failed: player HP is " + player.getHP());
                Checks.that(player.getResource() == 2, "T2 failed: resource is " + player.getResource());
                System.out.println("[Week 2] T2 / W02-ENC-01 passed");

                // [Week 2] T3 / W02-ENC-02: new enemy starts fresh
                Checks.that(enemyA.isDead(), "T3 failed: enemy A HP is " + enemyA.getHP());
                Checks.that(enemyB.getHP() == 45, "T3 failed: enemy B HP is " + enemyB.getHP());
                System.out.println("[Week 2] T3 / W02-ENC-02 passed");

                // [Week 2] T4 / W02-ENC-03: old defense is cleared
                Combatant p7 = new Combatant("P", 100);
                p7.defend();
                p7.clear_defense();
                Checks.that(!p7.isDefending(), "T4 failed: defense still pending");
                p7.take_damage(10);
                Checks.that(p7.getHP() == 90, "T4 failed: old defense reduced damage, HP is " + p7.getHP());
                System.out.println("[Week 2] T4 / W02-ENC-03 passed");

                // [Week 1 + Week 2] T5 / W01-INV-01: special at 2 is rejected
                Combatant p6 = new Combatant("P", 100, 2);
                Checks.that(!p6.special(), "T5 failed: special at 2 was accepted");
                Checks.that(p6.getResource() == 2 && p6.getHP() == 100, "T5 failed: state changed");
                System.out.println("[Week 2] T5 / W01-INV-01 passed (counterattack part: manual)");

                // [Week 2] EDGE-HP-5: attack on 5 HP ends at 0, not negative
                Combatant e8 = new Combatant("E", 5);
                e8.take_damage(10);
                Checks.that(e8.getHP() == 0, "EDGE-HP-5 failed: enemy HP is " + e8.getHP());
                System.out.println("[Week 2] EDGE-HP-5 passed");

                // ======== Week 3 ========

                // [Week 3] W03-RWD-01: player at 90 HP selects Recovery -> 100
                Combatant r1 = new Combatant("P", 100);
                r1.take_damage(10);
                r1.applyReward(Main.reward.HEAL);
                Checks.that(r1.getHP() == 100, "W03-RWD-01 failed: HP is " + r1.getHP());
                System.out.println("[Week 3] W03-RWD-01 passed");
                        
                // [Week 3] EDGE-HEAL-CAP: Recovery at 100 HP stays 100
                Combatant r2 = new Combatant("P", 100);
                r2.applyReward(Main.reward.HEAL);
                Checks.that(r2.getHP() == 100, "EDGE-HEAL-CAP failed: HP is " + r2.getHP());
                System.out.println("[Week 3] EDGE-HEAL-CAP passed");
                        
                // [Week 3] W03-RWD-02: Power -> attack 12, HP/resource unchanged
                Combatant r3 = new Combatant("P", 100, 2);
                Combatant e9 = new Combatant("E", 45);
                r3.take_damage(20);
                r3.applyReward(Main.reward.attackBOOST);
                Checks.that(r3.getattack() == 12, "W03-RWD-02 failed: attack is " + r3.getattack());
                Checks.that(r3.getHP() == 80 && r3.getResource() == 2, "W03-RWD-02 failed: HP/resource changed");
                e9.take_damage(r3.getattack());
                Checks.that(e9.getHP() == 33, "W03-RWD-02 failed: enemy HP is " + e9.getHP());
                System.out.println("[Week 3] W03-RWD-02 passed");
                        
                // [Week 3] T6: a second reward is rejected and changes nothing
                Combatant r4 = new Combatant("P", 100);
                r4.take_damage(30);
                Checks.that(r4.applyReward(Main.reward.attackBOOST), "T6 failed: first reward rejected");
                Checks.that(!r4.applyReward(Main.reward.HEAL), "T6 failed: second reward accepted");
                Checks.that(r4.getHP() == 70 && r4.getattack() == 12, "T6 failed: state changed");
                System.out.println("[Week 3] T6 passed");

                // W03-RWD-03/04/05 + W03-END-01: manual — reward code runs once, only after winning Encounter 1
                System.out.println("[Week 3] W03-RWD-03/04/05, W03-END-01: manual (see Main.main)");
                            }}