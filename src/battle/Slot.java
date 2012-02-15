package battle;

import gui.Tools;
import jpkmn.Driver;
import pokemon.*;
import pokemon.move.*;
import pokemon.Status.Effect;

/**
 * A slot in a battle. Abstracts the difference between a user-controlled party
 * and an AI controlled party. Slot implements the specifics of a battle. Many
 * methods here go by the same name as others whose classes have a field in
 * Slot. Use Slot's version to abstract the difference between human and AI, and
 * maintain important data inside the Slot class.
 */
public class Slot {
  public Pokemon leader;
  public Party party;
  public Field field;
  public Slot enemy;
  private Move move;
  private int bidedamage;
  private boolean human = false, bide = false;

  public Slot(Party p, Boolean humancontrol) {
    party = p;
    leader = p.leader();
    field = new Field(this);
    human = humancontrol;
  }

  public void set() {
    leader = party.pkmn[0];
    field.resetEffects();
  }

  /**
   * Picks moves. If this.ishuman, asks using gui.Tools, else random.
   * 
   * @return True if a move was picked
   */
  public boolean chooseMove() {
    if (!canChooseMove()) {
      Driver.log(Slot.class, leader.name + " Can't pick new move. Forced = "
          + move.name);
      return true;
    }

    if (human) {
      int a = Tools.selectMove(leader);
      if (a == -1) return false;

      move = leader.move[a];

      Driver.log(Slot.class, "User selected move to be : " + move.name);

      return true;
    }
    else {
      move = leader.move[(int) (Math.random() * leader.numMoves())];

      Driver.log(Slot.class, "Enemy move selected randomly to be : "
          + move.name);

      return true;
    }
  }

  private boolean canChooseMove() {
    // TODO : I haven't decided if this is correct
    return !leader.status.contains(Effect.WAIT);
  }

  public Move getMove() {
    return move;
  }

  /**
   * Launches an attack on the enemy slot.
   */
  public void attack() {
    Driver.log(Slot.class, leader.name + " beginning attack. Move = "
        + move.name);

    // Move # 9 (Bide) special handling
    if (bide) {
      enemy.takeDamageAbsolute("Bide damage is constant.", bidedamage * 2);
      bide = false;
      bidedamage = 0;
      return;
    }

    // 1. Measure PP for validity
    // 2. Measure if the user can attack
    // 3. Reduce PP
    // 4. Measure accuracy
    // Don't perform any if they didn't choose this move
    if (canChooseMove()) {
      // 1
      if (move.pp <= 0) {
        Tools.notify(leader, "CANNOT ATTACK", move.name
            + " does not have enough PP!");
        Driver.log(Slot.class, leader.name + " cannot attack. No PP.");
        return;
      }

      // 2
      if (!leader.canAttack()) {
        Tools.notify(leader, "CANNOT ATTACK", leader.name
            + " cannot attack because of " + leader.status.effectsToString());
        Driver.log(
            Slot.class,
            leader.name + " cannot perform attack. Causes = "
                + leader.status.effectsToString());
        return;
      }

      // 3
      if (human) {
        Driver.log(Slot.class, "Human controlled. PP reduced on move : "
            + move.name);
        move.enabled = move.pp-- == 1;
      }
      else {
        Driver.log(Slot.class, "!Human controlled. PP not reduced on move : "
            + move.name);
      }

      // 4
      if (!move.hits(enemy.leader)) {
        Driver.log(Slot.class, move.name + " missed target: "
            + enemy.leader.name);

        // Move # 60 (Hi Jump Kick) and Move # 69 (Jump Kick)
        // Reduce user health by 1/2 if the attack misses
        if (move.number == 60 || move.number == 69) {
          int d = (Battle.computeDamage(move, leader, enemy.leader) / 8);
          takeDamageAbsolute(move.name + " failure hurts the user", d);
          Driver.log(Slot.class, leader.name + " received recoil damage (" + d
              + "). Move was : " + move.name);
        }

        return;
      }
    }
    else {
      Driver.log(Slot.class, "PP and ability to attack not measured. "
          + leader.name + " did not choose move : " + move.name);
    }

    // Now comes the fun part
    if (move.style == MoveStyle.PHYSICAL || move.style == MoveStyle.SPECIAL
        || move.style == MoveStyle.OHKO) {
      enemy.takeDamage(Battle.computeDamage(move, leader, enemy.leader));
    }
    else if (move.style == MoveStyle.DELAY) {
      if (leader.status.contains(Effect.WAIT)) {
        leader.status.remove(Effect.WAIT);
        if (!leader.status.contains(Effect.WAIT)
            && move.style.attackAfterDelay)
          enemy.takeDamage(Battle.computeDamage(move, leader, enemy.leader));
      }
      else {
        for (int i = 0; i < move.style.delay; ++i) {
          leader.status.addEffect(Effect.WAIT);
        }
        if (!move.style.attackAfterDelay)
          enemy.takeDamage(Battle.computeDamage(move, leader, enemy.leader));
      }
    }
    else if (move.style == MoveStyle.REPEAT) {
      int damage = Battle.computeDamage(move, leader, enemy.leader);
      int repeatnum = (int) (MoveStyle.REPEAT_MIN
          + (Math.random() * MoveStyle.REPEAT_EXTRA + .35));
      System.out.println("REPEAT NUM = "+repeatnum);
      for (int i = 0; i < repeatnum; ++i)
        enemy.takeDamage(damage);
    }
    else if (move.style == MoveStyle.EFFECT) {
      for (BonusEffect be : move.be) {

        // Move # 73 (Leech Seed) fix
        if (be == BonusEffect.LEECH) {
          enemy.leader.status.addEffect(Status.Effect.SEEDED);
          leader.status.addEffect(Status.Effect.SEEDUSR);
        }
        else if (be.target == Target.SELF) {
          be.effect(leader);
        }
        else {
          be.effect(enemy.leader);
        }
      }
    }
    else { // Misc
      // TODO : This crap
      System.out.println("hello world.");
    }

  }

  /**
   * Works in tandem with attack to distribute damage to the leader.
   */
  private void takeDamage(int d) {

    // If measuring bide damage, record it
    if (bide) bidedamage += d;

    // Account for shielding
    if (field.contains(Field.Effect.PHYSSHIELD)
        && enemy.move.style == MoveStyle.PHYSICAL) {
      d /= 2;
    }
    else if (field.contains(Field.Effect.SPECSHIELD)
        && enemy.move.style == MoveStyle.SPECIAL) {
      d /= 2;
    }
    else if (field.contains(Field.Effect.INVULNERABLE)) {
      if (field.isImmune(enemy.move.name)) {
        d = 0;
        reportDamage(leader.name + " is invulnerable!", d);
        field.rollDownDuration();
        return; // reportDamage is special. don't keep going
      }
      else
        // Have immunity, but not to the move name
        // E.g. Dig/Rollout
        d *= 2;
    }

    reportDamage(null, d);
    leader.takeDamage(d);

    field.rollDownDuration();
  }

  /**
   * Works in tandem with attack to distribute damage to the leader. Ignores
   * shields and immunities. E.g. Bide/Dragon Rage/Immunity
   * 
   * @param s Additional text for reportDamage
   * @param d Damage taken
   */
  private void takeDamageAbsolute(String s, int d) {

    leader.takeDamage(d);

    reportDamage(s, d);
    field.rollDownDuration();
  }

  public void applyEffects() {
    // TODO : I haven't decided if this is correct
    leader.status.applyEffects();
  }

  /**
   * Calls party.doSwap(). Necessary to abstract human/AI. For AI, steps through
   * to find the first awake Pokemon, and switches with the leader.
   * 
   * @return True if a swap was made
   */
  public boolean doSwap() {
    Driver.log(Slot.class, "doSwap called. Human control = " + human);
    if (human) {
      if (party.doSwap()) {
        bide = false;
        bidedamage = 0;
        return true;
      }
      else {
        return false;
      }
    }
    else {
      for (int i = 1; i < 6 && party.pkmn[i] != null; ++i) {
        if (party.pkmn[i].awake) {
          Pokemon swap = party.pkmn[0];
          party.pkmn[0] = party.pkmn[i];
          party.pkmn[i] = swap;
          return true;
        }
      }
      return false;
    }
  }

  /**
   * Reports how hard the enemy hit the leader
   * 
   * @param s (Optional) for takeDamageAbsolute, specify special message
   * @param d Damage taken by this.leader
   */
  private void reportDamage(String s, int d) {
    String text = enemy.leader.name + " used " + enemy.move.name + "!\n";
    if (s == null) {
      if (enemy.move.effectiveness(leader) > 1.0)
        text += "It's super effective!\n";
      else if (enemy.move.effectiveness(leader) == 0)
        text += "It failed!\n";
      else if (enemy.move.effectiveness(leader) < 1)
        text += "It's not very effective...\n";
      text += leader.name + " took " + d + " damage!";
    }
    else {
      text += s;
    }

    Tools.notify(leader, "DAMAGE", text);
  }
}
