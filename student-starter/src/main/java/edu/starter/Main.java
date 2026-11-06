package edu.starter;

import java.util.Random;
import java.util.Scanner;

/** Student-owned game entry point; replace this message with your own implementation. */
public final class Main {
    

    static class Combatant {
        private String name;
        private int max_hp;
        private int hp;
        private boolean pending_defense;
        private int resources;

        Combatant(String name, int max_hp) {
            this.name = name;
            this.max_hp = max_hp;
            this.hp = max_hp;        
            this.pending_defense = false;
            this.resources = 5;
        }
        Combatant(String name, int max_hp, int resources) {
            this(name, max_hp);
            if (resources < 0 || resources > 10) {
                throw new IllegalArgumentException("resources must be 0-10, got " + resources);
            }
            this.resources = resources;
        }

        boolean isDefending() {
            return this.pending_defense;
        }
        int getResource() {
            return this.resources;
            }
        int getHP() {
            return this.hp;}
        boolean clear_defense() {
            if (this.pending_defense) {
                this.pending_defense = false;
                return true;}
            else {return false;}}
        boolean isDead() {
            return this.hp <= 0;}
        void take_damage(int damage) {
            if (this.pending_defense) {
                damage = damage - 5;
                if (damage < 0) {damage = 0;}
                this.pending_defense = false;}
            this.hp -= damage;
            if (this.hp < 0) {
                this.hp = 0;}}
        void defend() {
            this.pending_defense = true;}
        boolean special() {
            if (this.resources > 2) {
                this.resources -= 3;
                return true;}
            else {return false;}}
        boolean recover() {
            if (this.resources < 8) {
                this.resources += 3;
                return true;}
                else if (this.resources < 10) {this.resources = 10;return true;}
                else {
                return false;}}
                }  
        static int encounter = 1;  
        static boolean playBattle(Combatant player, Combatant enemy, Scanner in) {
            System.out.println("=== Encounter " + encounter + " ===");
            Runnable[] enemyMoves = {
                () -> player.take_damage(10),};
            int turn = 0;
            Random rng = new Random();
            while (!enemy.isDead() && !player.isDead()) {
                System.out.println("press 1 to attack, 2 to defend, 3 to use your special, 4 to recover your resource");
                System.out.println("Player HP: " + player.getHP() + " | Resource: " + player.getResource() + " | Enemy HP: " + enemy.getHP());
                int past_turn = turn;
                int choice = ConsoleChoice.read(in, 1, 4);
                if (choice == -1) {System.out.println("ilegal input please try again");}
                if (choice == 1) {enemy.take_damage(10); turn +=1;}
                if (choice == 2) {player.defend(); turn +=1;}
                if (choice == 4) {if (player.recover()) {turn +=1;}}
                if (choice == 3) {if (player.special()) {enemy.take_damage(15);;turn +=1;}};
                if (!enemy.isDead() && turn > past_turn) {enemyMoves[rng.nextInt(enemyMoves.length)].run();}}
                encounter += 1;
            if (player.isDead()) {System.out.println("You have been defeated!"); return false;}
            else{System.out.println("You have killed the enemy");return true;}
}
            public static void main(String[] args) {
                Scanner in = new Scanner(System.in);
                if (args.length > 0 && args[0].equals("test")) {
                    runTests();
                    return;}
                Combatant player = new Combatant("player", 100);
                Combatant enemyA = new Combatant("enemy", 30);
                Combatant enemyB = new Combatant("enemy", 45);
                if (playBattle(player, enemyA, in)) { player.clear_defense(); if (playBattle(player, enemyB, in)){System.out.println("you have won good job");}}
            }
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
            }
        }
    
