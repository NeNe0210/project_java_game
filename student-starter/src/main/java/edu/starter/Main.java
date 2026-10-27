package edu.starter;

import java.util.Random;
import java.util.Scanner;

/** Student-owned game entry point; replace this message with your own implementation. */
public final class Main {

    static class Things {
        int player_hp;
        int enemy_hp;
        boolean pending_defense;
        int player_resources;

        Things() {
            this.player_hp = 100;
            this.enemy_hp = 100;
            this.pending_defense = false;
            this.player_resources = 5;}
        boolean isPlayerDead() {
            return this.player_hp <= 0;}
        void attack(int damage) {
            this.enemy_hp -= damage;}
        void counterattack(int damage) {
            if (this.pending_defense == true){
                damage -= 5;
                if (damage < 0){damage = 0;}
                this.player_hp -= damage;
                this.pending_defense = false;}
            else {this.player_hp -= damage;}}
        void defend() {
            this.pending_defense = true;}
        boolean special() {
            if (this.player_resources > 2) {
                this.player_resources -= 3;
                attack(15); 
                return true;}
            else {return false;}}
        void recover() {
            if (this.player_resources < 8) {
                this.player_resources += 3;}
                else {
                this.player_resources = 10;}}
                }    
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        Things state = new Things();
        Runnable[] enemyMoves = {
            () -> state.counterattack(10),};
        int turn = 0;
        Random rng = new Random();
        while ( state.player_hp > 0 && state.enemy_hp > 0) {
            int past_turn = turn;
            int choice = ConsoleChoice.read(in, 1, 4);
            if (choice == 1) {state.attack(10); turn +=1;}
            if (choice == 2) {state.defend(); turn +=1;}
            if (choice == 4) {state.recover(); turn +=1;}
            if (choice == 3) {if (state.special()) {turn +=1;}}
            if (state.enemy_hp > 0 && turn > past_turn) {enemyMoves[rng.nextInt(enemyMoves.length)].run();}
            System.out.println(state.player_hp);
            System.out.println(state.enemy_hp);
            System.out.println(turn);
            System.out.println(state.player_resources);
            System.out.println(turn);
        }
     }  }