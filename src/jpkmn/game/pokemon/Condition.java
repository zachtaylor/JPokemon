package jpkmn.game.pokemon;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.battle.Battle;

public class Condition {
  /**
   * Creates a new Condition for the specified Pokemon
   * 
   * @param p The Pokemon whom this status effects
   */
  public Condition(Pokemon p) {
    pkmn = p;
  }

  /**
   * Computes the catch bonus for effects on the Pokemon. 20 if FRZ or SLP, 15
   * if BRN, PSN, or PAR. 10 normally.
   * 
   * @return the Catch Bonus
   */
  public int getCatchBonus() {
    int best = 10;

    for (ConditionEffect i : issues) {
      if (i == ConditionEffect.FREEZE || i == ConditionEffect.SLEEP)
        return 20;
      else if (i == ConditionEffect.BURN || i == ConditionEffect.POISON || i == ConditionEffect.PARALYZE)
        best = 15;
    }

    return best;
  }

  /**
   * Adds a new issue to the Pokemon. If already afflicted, the new one is not
   * added, except for WAITs (they can stack). If the Pokemon is BURNed or
   * PARALYZEd, stats are adjusted accordingly.
   * 
   * @param i The issue to be added
   */
  public void add(ConditionEffect i) {
    if (i != ConditionEffect.WAIT && contains(i))
      remove(i);

    issues.add(i);
  }

  /**
   * Checks against the user's effects to see if they can attack. Returns true
   * if they can.
   * 
   * @return true if user can attack
   */
  public boolean canAttack() {
    for (ConditionEffect i : issues) {
      if (i == ConditionEffect.FREEZE || i == ConditionEffect.SLEEP || i == ConditionEffect.FLINCH)
        return false;
      else if (i == ConditionEffect.PARALYZE && Math.random() < .25)
        return false;
      else if (i == ConditionEffect.CONFUSE) {
        attackself = true;
        return false;
      }
    }

    return true;
  }

  /**
   * Applies issues. DOTs hurt, Flinch is removed, volatile effects have a
   * chance to dispel.
   */
  public String[] applyEffects() {
    List<String> messages = new ArrayList<String>();

    for (ConditionEffect current : issues) {
      if (current == ConditionEffect.BURN) {
        pkmn.takeDamage(pkmn.maxHealth() / 10);
        messages.add(pkmn.name() + " was injured by it's burn!");
      }
      else if (current == ConditionEffect.WRAP) {
        if (Math.random() > .66666) {
          remove(ConditionEffect.WRAP);
          messages.add(pkmn.name() + " freed itself!");
        }
        else {
          pkmn.takeDamage(pkmn.maxHealth() / 10);
          messages.add(pkmn.name() + " was injured by the binding!");
        }
      }
      else if (current == ConditionEffect.CONFUSE) {
        if (attackself) {
          pkmn.takeDamage(Battle.confusedDamage(pkmn));
          attackself = false;
        }
        else if (Math.random() > .66666) {
          remove(ConditionEffect.CONFUSE);
          messages.add(pkmn.name() + " is no longer confused!");
        }
      }
      else if (current == ConditionEffect.FLINCH) {
        remove(ConditionEffect.FLINCH);
      }
      else if (current == ConditionEffect.FREEZE && Math.random() > .8) {
        remove(ConditionEffect.FREEZE);
        messages.add(pkmn.name() + " broke out of the ice!");
      }
      else if (current == ConditionEffect.POISON) {
        pkmn.takeDamage(pkmn.maxHealth() / 10);
        messages.add(pkmn.name() + " was injured by the poison!");
      }
      else if (current == ConditionEffect.SLEEP && Math.random() > .333333) {
        remove(ConditionEffect.SLEEP);
        messages.add(pkmn.name() + " woke up!");
      }
    }

    return messages.toArray(new String[messages.size()]);
  }

  /**
   * Cleans up all status ailments.
   */
  public void reset() {
    issues = new ArrayList<ConditionEffect>();
  }

  public boolean contains(ConditionEffect i) {
    return issues.contains(i);
  }

  public String toString() {
    if (issues.isEmpty())
      return "";
    return issues.toString();
  }

  public boolean remove(ConditionEffect i) {
    return issues.remove(i);
  }

  private Pokemon pkmn;
  private boolean attackself;
  private List<ConditionEffect> issues = new ArrayList<ConditionEffect>();
}