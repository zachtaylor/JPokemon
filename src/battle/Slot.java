package battle;

import java.util.ArrayList;

import gui.Tools;
import jpkmn.Driver;
import pokemon.*;
import pokemon.move.*;
import pokemon.Condition.Issue;

/**
 * A slot in a battle. Abstracts the difference between a user-controlled party
 * and an AI controlled party. Slot implements the specifics of a battle. Many
 * methods here go by the same name as others whose classes have a field in
 * Slot. Use Slot's version to abstract the difference between human and AI,
 * and maintain important data inside the Slot class.
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
    leader = p.getLeader();
    field = new Field(this);
    human = humancontrol;
  }

  public void set() {
    leader = party.getLeader();
    field.resetEffects();
  }

  /**
   * Picks moves. If this.ishuman, asks using gui.Tools, else random.
   * 
   * @return True if a move was picked
   */
  public boolean chooseMove() {
    if (!canChooseMove()) {
      Driver.log(Slot.class, leader.name() + " Can't pick new move. Forced = "
          + move.name);
      return true;
    }

    if (human) {
      int a = Tools.askMove(leader, null);
      if (a == -1) return false;

      move = leader.move[a];

      Driver.log(Slot.class, "User " + leader.name + " selected move to be : "
          + move.toString());

      return true;
    }
    else {
      move = leader.move[(int) (Math.random() * leader.numMoves())];

      Driver.log(Slot.class, "Enemy " + leader.name
          + " move selected randomly to be : " + move.toString());

      return true;
    }
  }

  private boolean canChooseMove() {
    return !leader.condition.contains(Issue.WAIT);
  }

  public Move getMove() {
    return move;
  }

  /**
   * Launches an attack on the enemy slot.
   */
  public void attack() {
    ArrayList<String> text = new ArrayList<String>();
    int damage;

    // Move # 9 (Bide) special handling
    if (bide) {
      enemy.takeDamageAbsolute(bidedamage * 2);
      bide = false;
      bidedamage = 0;
      return;
    }

    // Don't perform any if they didn't choose this move
    if (canChooseMove()) {
      // 1 Measure PP for validity
      if (move.pp <= 0) {
        text.add("CANNOT ATTACK");
        text.add(move.name + " does not have enough PP!");
        reportDamage(leader, text);
        return;
      }

      // 2 Measure if the user can attack
      if (!leader.canAttack()) {
        text.add("CANNOT ATTACK");
        text.add(leader.name + " cannot attack because of "
            + leader.condition.effectsToString());
        reportDamage(leader, text);
        return;
      }

      // 3 Reduce PP
      if (human) move.enabled = --move.pp == 0;

      // 4 Measure accuracy
      if (!move.hits(enemy.leader)) {
        text.add("MISS");
        text.add(leader.name + " used " + move.name + ", but it missed!");

        // Move # 60 (Hi Jump Kick) and Move # 69 (Jump Kick) hurt on miss
        if (move.number == 60 || move.number == 69) {
          damage = (Battle.computeDamage(move, leader, enemy.leader) / 8);
          takeDamageAbsolute(damage);
          text.add(leader.name + " received " + damage + " recoil damage from "
              + move.name);
        }

        reportDamage(leader, text);
        return;
      }
    }
    else {
      Driver.log(Slot.class, "PP and ability to attack not measured. "
          + leader.name + " did not choose move : " + move.name);
    }

    if (move.style == MoveStyle.PHYSICAL || move.style == MoveStyle.SPECIAL
        || move.style == MoveStyle.OHKO) {
      damage = Battle.computeDamage(move, leader, enemy.leader);
      enemy.takeDamage(damage);
    }

    else if (move.style == MoveStyle.DELAY) {
      if (leader.condition.contains(Issue.WAIT)) {

        leader.condition.remove(Issue.WAIT); // take away 1 wait

        if (!leader.condition.contains(Issue.WAIT) && move.style.attackAfterDelay) {
          damage = Battle.computeDamage(move, leader, enemy.leader);
          enemy.takeDamage(damage);
        }
        else { // Already attacked before delay || resting this turn
          text.add("WAITING");
          text.add(leader.name + " used " + move.name + "!");
          text.add(leader.name + " is resting this turn.");
          reportDamage(leader, text);
        }

      }
      else {

        for (int i = 0; i < move.style.delay; ++i)
          leader.condition.addIssue(Issue.WAIT); // add all the waits

        if (!move.style.attackAfterDelay) {
          damage = Battle.computeDamage(move, leader, enemy.leader);
          enemy.takeDamage(damage);
        }
        else { // attack after delay. resting this turn
          text.add("WAITING");
          text.add(leader.name + " used " + move.name + "!");
          text.add(leader.name + " is waiting this turn.");
          reportDamage(leader, text);
        }
      }
    }

    else if (move.style == MoveStyle.REPEAT) {
      damage = Battle.computeDamage(move, leader, enemy.leader);
      int repeatnum = (int) (MoveStyle.REPEAT_MIN + (Math.random()
          * MoveStyle.REPEAT_EXTRA + .35));
      enemy.takeDamage(damage * repeatnum);
    }

    else if (move.style == MoveStyle.EFFECT) {
      text.add("EFFECTS");
      text.add(leader.name + " used " + move.name + "!");

      for (BonusEffect be : move.be) {
        // Move # 73 (Leech Seed) fix cause it targets both user and enemy
        if (be == BonusEffect.LEECH) {
          enemy.leader.condition.addIssue(Condition.Issue.SEEDED);
          leader.condition.addIssue(Condition.Issue.SEEDUSR);
        }
        else if (be.target == Target.SELF) {
          text.add(leader.name + " is now " + be.toString());
          be.effect(leader);
        }
        else {
          text.add(enemy.leader.name + " is now " + be.toString());
          be.effect(enemy.leader);
        }
      }

      reportDamage(leader, text);
    }

    else { // Misc
      Tools.notify("err", move.name + " doesn't work yet. Sorry about that.",
          "Please write this up so I can work on it.");
    }
  }

  /**
   * Works in tandem with attack to distribute damage to the leader. Reports
   * what happened.
   * 
   * @param d Damage taken
   */
  private void takeDamage(int d) {

    ArrayList<String> text = new ArrayList<String>();
    text.add("DAMAGE");
    text.add(enemy.leader.name + " used " + enemy.move.name + "!");

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
        field.rollDownDuration();
        text.add(leader.name + " is invulnerable to " + enemy.move.name);
      }
      else
        // Have immunity, but not to the move name
        // E.g. Dig/Rollout
        d *= 2;
      text.add(leader.name + " took " + d + " damage.");
    }

    text.add(leader.name + " took " + d + " damage.");

    if (enemy.move.effectiveness(leader) > 1.0)
      text.add("It's super effective!");
    else if (enemy.move.effectiveness(leader) == 0)
      text.add("It failed!");
    else if (enemy.move.effectiveness(leader) < 1)
      text.add("It's not very effective...");

    reportDamage(enemy.leader, text);

    takeDamageAbsolute(d);
  }

  /**
   * Works in tandem with attack to distribute damage to the leader. Ignores
   * shields and immunities. Does not report anything. USES: Bide/Dragon
   * Rage/Sonicboom/Super Fang
   * 
   * @param d Damage taken
   */
  private void takeDamageAbsolute(int d) {
    leader.takeDamage(d);

    field.rollDownDuration();
  }

  public void applyEffects() {
    leader.condition.applyEffects();
  }

  /**
   * Applies bonus effects from the current move to the enemy.
   * 
   * @param isFast True to allow making the opponent flinch
   */
  public void applyCurrentMoveEffects(boolean isFast) {
    for (BonusEffect b : move.be) {
      if (!(b == BonusEffect.FLINCH) || isFast) {
        b.effect(enemy.leader);
      }
    }
  }

  /**
   * Calls party.doSwap(). Necessary to abstract human/AI. For AI, steps
   * through to find the first awake Pokemon, and switches with the leader.
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
  private void reportDamage(Pokemon p, ArrayList<String> text) {
    Tools.notify(p, text.toArray(new String[text.size()]));
    Driver.log(Slot.class, text.toString() + " " + p.name + ": " + p.health.cur
        + "/" + p.health.max);
  }

}
