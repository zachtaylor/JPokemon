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
    lastMessage = new ArrayList<String>();
    effects = new ArrayList<ConditionEffect>();
  }

  /**
   * Computes the catch bonus for effects on the Pokemon. 20 if FRZ or SLP, 15
   * if BRN, PSN, or PAR. 10 normally.
   * 
   * @return the Catch Bonus
   */
  public int getCatchBonus() {
    int best = 10;

    for (ConditionEffect e : effects) {
      if (e == ConditionEffect.FREEZE || e == ConditionEffect.SLEEP)
        return 20;
      else if (e == ConditionEffect.BURN || e == ConditionEffect.POISON
          || e == ConditionEffect.PARALYZE)
        best = 15;
    }

    return best;
  }

  /**
   * Adds a new issue to the Pokemon. If already afflicted, the effect is
   * refreshed.
   * 
   * @param e The issue to be added
   */
  public void add(ConditionEffect e) {
    if (contains(e))
      remove(e);

    effects.add(e);
  }

  /**
   * Checks against the user's effects to see if they can attack. Returns true
   * if they can.
   * 
   * @return true if user can attack
   */
  public boolean canAttack() {
    for (ConditionEffect i : effects) {
      if (i == ConditionEffect.FREEZE || i == ConditionEffect.SLEEP
          || i == ConditionEffect.FLINCH)
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
  public void applyEffects() {
    resetMessage();

    for (ConditionEffect current : effects) {
      if (current == ConditionEffect.BURN) {
        pkmn.takeDamage(pkmn.maxHealth() / 10);
        pushMessage(pkmn.name() + " was injured by it's burn!");
      }
      else if (current == ConditionEffect.WRAP) {
        if (Math.random() > .66666) {
          remove(ConditionEffect.WRAP);
          pushMessage(pkmn.name() + " freed itself!");
        }
        else {
          pkmn.takeDamage(pkmn.maxHealth() / 10);
          pushMessage(pkmn.name() + " was injured by the binding!");
        }
      }
      else if (current == ConditionEffect.CONFUSE) {
        if (attackself) {
          pkmn.takeDamage(Battle.confusedDamage(pkmn));
          attackself = false;
        }
        else if (Math.random() > .66666) {
          remove(ConditionEffect.CONFUSE);
          pushMessage(pkmn.name() + " is no longer confused!");
        }
      }
      else if (current == ConditionEffect.FLINCH) {
        remove(ConditionEffect.FLINCH);
      }
      else if (current == ConditionEffect.FREEZE && Math.random() > .8) {
        remove(ConditionEffect.FREEZE);
        pushMessage(pkmn.name() + " broke out of the ice!");
      }
      else if (current == ConditionEffect.POISON) {
        pkmn.takeDamage(pkmn.maxHealth() / 10);
        pushMessage(pkmn.name() + " was injured by the poison!");
      }
      else if (current == ConditionEffect.SLEEP && Math.random() > .333333) {
        remove(ConditionEffect.SLEEP);
        pushMessage(pkmn.name() + " woke up!");
      }
    }
  }

  public String[] lastMessage() {
    return lastMessage.toArray(new String[lastMessage.size()]);
  }

  /**
   * Cleans up all status ailments.
   */
  public void reset() {
    effects = new ArrayList<ConditionEffect>();
  }

  public boolean contains(ConditionEffect e) {
    return effects.contains(e);
  }

  public String toString() {
    if (effects.isEmpty())
      return "";
    return effects.toString();
  }

  public boolean remove(ConditionEffect e) {
    return effects.remove(e);
  }

  private void resetMessage() {
    lastMessage = new ArrayList<String>();
  }

  private void pushMessage(String s) {
    lastMessage.add(s);
  }

  private Pokemon pkmn;
  private boolean attackself;
  private List<String> lastMessage;
  private List<ConditionEffect> effects;
}