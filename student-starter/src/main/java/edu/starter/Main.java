package edu.starter;

import java.util.Random;
import java.util.Scanner;

/** Student-owned game entry point; replace this message with your own implementation. */
public final class Main {
    
    /**
     * Invariant: 0 <= hp <= max_hp && 0 <= resources <= 10 && attack_damage >= 0
     *            (attack_damage holds only the POWER bonus; total attack == baseAttack() + attack_damage)
     */
    static class Standard {
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
         *       && attack_damage == 0 && resources == 5 && pending_defense == false
         */
        Standard(String name, int max_hp) {
            this.name = name;
            this.max_hp = max_hp;
            this.hp = max_hp;        
            this.attack_damage = 0;
            this.resources = 5;
        }
        /**
         * @pre max_hp > 0 && resources >= 0 && resources <= 10
         * @post this.resources == resources (all other fields as in the other constructor)
         */
        Standard(String name, int max_hp, int resources) {
            this(name, max_hp);
            if (resources < 0 || resources > 10) {
                throw new IllegalArgumentException("resources must be 0-10, got " + resources);
            }
            this.resources = resources;
        }
        /**
         * Damage blocked when defending. Subclasses override to change it.
         * @post $ret == 5
         */
        int blockAmount() {return 5;}
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
         * @post if $prev(pending_defense): hp == max(0, $prev(hp) - max(0, damage - blockAmount()))
         *       else:                      hp == max(0, $prev(hp) - damage)
         *       && pending_defense == false
         *       && resources == $prev(resources)
         */
        void take_damage(int damage) {
            if (this.pending_defense) {
                damage = damage - blockAmount();
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
         * Attack before POWER bonus. Subclasses override to change it.
         * @post $ret == 10
         */
        int baseAttack() { return 10; }
        /**
         * @post $ret == baseAttack() + attack_damage
         */
        int getattack() {
            return this.attack_damage + baseAttack();}
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
    /** Glass cannon profile: 80 HP, base attack 12, blocks 5. */
    static class Glass_canon extends Standard {
        @Override
        int baseAttack() { return 12; }
        Glass_canon(String name) {
            super(name, 80);
        }}
    /** Tank profile: 130 HP, base attack 8, blocks 7. */
    static class Tank extends Standard {
            @Override
            int baseAttack() { return 8; }
            @Override
            int blockAmount() { return 7; }
            Tank (String name) {
                super(name, 130);
            }}
        
        /** The two rewards the player can choose after winning Encounter 1. */

        enum reward {HEAL, attackBOOST
        }
    
        static int encounter = 1;  
        /**
         * @pre !player.isDead() && !enemy.isDead()
         * @post ($ret == true  && enemy.isDead() && !player.isDead())
         *       || ($ret == false && player.isDead())
         */
        static boolean playBattle(Standard player, Standard enemy, Scanner in) {
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
                Standard player = null;
                System.out.println("Welcome to the game! You are a hero who must defeat two enemies in a row.");
                System.out.println("You will be given a choice between three classes: Standard, Glass_canon, and Tank. Each class has different stats and abilities.");
                System.out.println("Please choose your class by typing the corresponding number:");
                System.out.println("1. Standard (HP: 100, Attack: 10, Block: 5, Resource: 5)");
                System.out.println("2. Glass_canon (HP: 80, Attack: 12, Block: 5, Resource: 5)");
                System.out.println("3. Tank (HP: 130, Attack: 8, Block: 7, Resource: 5)");
                int choice = ConsoleChoice.read(in, 1, 3);
                while (choice == -1) {
                    System.out.println("illegal input please try again");
                    choice = ConsoleChoice.read(in, 1, 3);}
                if (choice == 1) {player = new Standard("player", 100);}
                if (choice == 2) {player = new Glass_canon("player");}
                if (choice == 3) {player = new Tank("player");
                    
                }
                Standard enemyA = new Standard("enemy", 30);
                Standard enemyB = new Standard("enemy", 45);
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