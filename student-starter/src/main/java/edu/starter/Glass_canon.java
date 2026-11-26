package edu.starter;

/** Glass cannon profile: 80 HP, base attack 12, blocks 5. */
class Glass_canon extends Standard {
    @Override
    int baseAttack() { return 12; }
    Glass_canon(String name) {
        super(name, 80);
    }}
