package edu.starter;

/** Tank profile: 130 HP, base attack 8, blocks 7. */
class Tank extends Standard {
        @Override
        int baseAttack() { return 8; }
        @Override
        int blockAmount() { return 7; }
        Tank (String name) {
            super(name, 130);
        }}
