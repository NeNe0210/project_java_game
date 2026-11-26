package edu.starter;

import java.util.Random;
import java.util.Scanner;

/** Student-owned game entry point; replace this message with your own implementation. */
public final class Main {

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