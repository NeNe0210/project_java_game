package edu.starter;

/**
 * Invariant: 0 <= hp <= max_hp && 0 <= resources <= 10 && attack_damage >= 0
 *            (attack_damage holds only the POWER bonus; total attack == baseAttack() + attack_damage)
 */
class Standard {
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
