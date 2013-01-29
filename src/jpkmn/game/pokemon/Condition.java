package jpkmn.game.pokemon;

import java.util.ArrayList;
import java.util.List;

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
    double best = 10;

    for (ConditionEffect e : effects)
      best *= e.catchBonus();

    return (int) best;
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
      if (i.blocksAttack())
        return false;
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
      boolean persist = Math.random() <= current.persistanceChance();
      String message = current.persistanceMessage(persist);

      if (persist)
        pkmn.takeDamage((int) (pkmn.maxHealth() * current.damagePercentage()));
      else
        remove(current);

      if (message != null)
        pushMessage(pkmn.name() + message);
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
  private List<String> lastMessage;
  private List<ConditionEffect> effects;
}