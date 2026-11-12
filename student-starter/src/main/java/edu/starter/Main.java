package edu.starter;

import java.util.Random;
import java.util.Scanner;

/** Student-owned game entry point; replace this message with your own implementation. */
public final class Main {
    
    /**
     * Invariant: 0 <= hp <= max_hp && 0 <= resources <= 10 && attack_damage >= 10
     */
    static class Combatant {
        private String name;
        private int max_hp;
        private int hp;
        private int attack_damage;
        private int resources;
        private boolean pending_defense = false;
        private boolean rewardClaimed = false;
        /**
         * @pre max_hp > 0
         * @post this.name == name && this.max_hp == max_hp && hp == max_hp
         *       && attack_damage == 10 && resources == 5 && pending_defense == false
         */
        Combatant(String name, int max_hp) {
            this.name = name;
            this.max_hp = max_hp;
            this.hp = max_hp;        
            this.attack_damage = 10;
            this.resources = 5;
        }
        /**
         * @pre max_hp > 0 && resources >= 0 && resources <= 10
         * @post this.resources == resources (all other fields as in the other constructor)
         */
        Combatant(String name, int max_hp, int resources) {
            this(name, max_hp);
            if (resources < 0 || resources > 10) {
                throw new IllegalArgumentException("resources must be 0-10, got " + resources);
            }
            this.resources = resources;
        }
        /**
         * @post $ret == pending_defense
         */
        boolean isDefending() {
            return this.pending_defense;
        }
        /**
         * @post $ret == resources
         */
        int getResource() {
            return this.resources;
            }
        /**
         * @post $ret == hp
         */
        int getHP() {
            return this.hp;}
        /**
         * @post pending_defense == false && $ret == $prev(pending_defense)
        */
        boolean clear_defense() {
            if (this.pending_defense) {
                this.pending_defense = false;
                return true;}
            else {return false;}}
        /**
         * @post $ret == (hp <= 0)
         */
        boolean isDead() {
            return this.hp <= 0;}
        /**
         * @pre damage >= 0
         * @post if $prev(pending_defense): hp == max(0, $prev(hp) - max(0, damage - 5))
         *       else:                      hp == max(0, $prev(hp) - damage)
         *       && pending_defense == false
         *       && resources == $prev(resources)
         */
        void take_damage(int damage) {
            if (this.pending_defense) {
                damage = damage - 5;
                if (damage < 0) {damage = 0;}
                this.pending_defense = false;}
            this.hp -= damage;
            if (this.hp < 0) {
                this.hp = 0;}}
        /**
         * @post pending_defense == true && hp == $prev(hp) && resources == $prev(resources)
         */
        void defend() {
            this.pending_defense = true;}
        /**
         * @post if $prev(resources) >= 3: resources == $prev(resources) - 3 && $ret == true
         *       else:                     resources == $prev(resources) && $ret == false
         */
        boolean special() {
            if (this.resources > 2) {
                this.resources -= 3;
                return true;}
            else {return false;}}
        /**
         * @post if $prev(resources) < 8:       resources == $prev(resources) + 3 && $ret == true
         *       else if $prev(resources) < 10: resources == 10 && $ret == true
         *       else:                          resources == $prev(resources) && $ret == false
         */
        boolean recover() {
            if (this.resources < 8) {
                this.resources += 3;
                return true;}
                else if (this.resources < 10) {this.resources = 10;return true;}
                else {
                return false;}}
        /**
         * @post $ret == attack_damage
         */
        int getattack() {
            return this.attack_damage;}
        /**
         * @post hp == min(max_hp, $prev(hp) + 25) && resources == $prev(resources)
         *       && attack_damage == $prev(attack_damage)
         */
        private void heal() {
            this.hp += 25;
            if (this.hp > this.max_hp) {this.hp = this.max_hp;}}
        /**
         * @post attack_damage == $prev(attack_damage) + 2
         *       && hp == $prev(hp) && resources == $prev(resources)
         */
        private void attackBoost() {this.attack_damage += 2;}
        
        /**
 * @pre r != null
 * @post if !$prev(rewardClaimed): the reward r is applied, rewardClaimed == true, $ret == true
 *       else: nothing changes, $ret == false
 */
        boolean applyReward(reward r) {
        if (this.rewardClaimed) {return false;}
        if (r == reward.HEAL) {heal();}
        else {attackBoost();}
        this.rewardClaimed = true;
        return true;}
        }
        /** The two rewards the player can choose after winning Encounter 1. */

        enum reward {HEAL, attackBOOST
        }
    
        static int encounter = 1;  
        /**
         * @pre !player.isDead() && !enemy.isDead()
         * @post ($ret == true  && enemy.isDead() && !player.isDead())
         *       || ($ret == false && player.isDead())
         */
        static boolean playBattle(Combatant player, Combatant enemy, Scanner in) {
            System.out.println("=== Encounter " + encounter + " ===");
            Runnable[] enemyMoves = {
                () -> player.take_damage(10),};
            int turn = 0;
            Random rng = new Random();
            while (!enemy.isDead() && !player.isDead()) {
                System.out.println("press 1 to attack, 2 to defend, 3 to use your special, 4 to recover your resource");
                System.out.println("Player HP: " + player.getHP() + " | Resource: " + player.getResource() + " | Enemy HP: " + enemy.getHP() + " | Attack: " + player.getattack());
                int past_turn = turn;
                int choice = ConsoleChoice.read(in, 1, 4);
                if (choice == -1) {System.out.println("illegal input please try again");}
                if (choice == 1) {enemy.take_damage(player.getattack()); turn +=1;}
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
                    Tests.runTests();
                    return;}
                Combatant player = new Combatant("player", 100);
                Combatant enemyA = new Combatant("enemy", 30);
                Combatant enemyB = new Combatant("enemy", 45);
                if (playBattle(player, enemyA, in)) {
                    System.out.println("press 1 for Potion (+25 HP), press 2 for X Attack (+2 Attack)");
                    int r = ConsoleChoice.read(in, 1, 2);
                    while (r == -1) {
                        System.out.println("illegal input please try again");
                        r = ConsoleChoice.read(in, 1, 2);}
                    reward chosen = (r == 1) ? reward.HEAL : reward.attackBOOST;
                    player.applyReward(chosen);
                    player.clear_defense();
                    if (playBattle(player, enemyB, in)) {System.out.println("you have won good job");}
                }
            }
        }