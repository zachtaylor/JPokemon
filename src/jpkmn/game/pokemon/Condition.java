package jpkmn.game.pokemon;

import java.util.ArrayList;

public class Condition {
  private int a; // flag to do work

  public enum Issue {
    BURN, PARALYZE, SLEEP, POISON, SEEDED, WRAP, SEEDUSR, FREEZE, CONFUSE,
    FLINCH, WAIT;
  }

  /**
   * Creates a new Status for the specified Pokemon
   * 
   * @param p The Pokemon whom this status effects
   */
  public Condition(Pokemon p) {
    pkmn = p;
    awake = true;
  }

  public boolean getAwake() {
    return awake;
  }

  public void setAwake(boolean b) {
    awake = b;
  }

  /**
   * Computes the catch bonus for effects on the Pokemon. 20 if FRZ or SLP, 15
   * if BRN, PSN, or PAR. 10 normally.
   * 
   * @return the Catch Bonus
   */
  public int getCatchBonus() {
    if (issues.contains(Issue.FREEZE) || issues.contains(Issue.SLEEP))
      return 20;
    else if (issues.contains(Issue.BURN) || issues.contains(Issue.POISON)
        || issues.contains(Issue.PARALYZE))
      return 15;
    else
      return 10;
  }

  /**
   * Adds a new issue to the Pokemon. If already afflicted, the new one is not
   * added, except for WAITs (they can stack). If the Pokemon is BURNed or
   * PARALYZEd, stats are adjusted accordingly.
   * 
   * @param i The issue to be added
   */
  public void addIssue(Issue i) {
    if (i == Issue.WAIT)
      issues.add(i);
    else if (!issues.contains(i)) {
      issues.add(i);

      // TODO Notify that effect is added

      pkmn.stats.effectBy(i);
    }
  }

  /**
   * Checks against the user's effects to see if they can attack. Returns true
   * if they can.
   * 
   * @return true if user can attack
   */
  public boolean canAttack() {
    if (issues.contains(Issue.FREEZE))
      return false;
    else if (issues.contains(Issue.SLEEP))
      return false;
    else if (issues.contains(Issue.FLINCH))
      return false;
    else if (issues.contains(Issue.PARALYZE) && Math.random() < .25)
      return false;
    else if (issues.contains(Issue.CONFUSE) && Math.random() < .33334) {
      attackself = true;
      return false;
    }

    return true;
  }

  /**
   * Applies issues. DOTs hurt, Flinch is removed, volatile effects have a
   * chance to dispel.
   */
  public void applyEffects() {
    // TODO Notifications for all status things

    for (Issue current : issues) {
      if (current == Issue.BURN) {
        pkmn.takeDamage(pkmn.stats.hp.max() / 10);
        // TODO Hurt by burn
      }
      else if (current == Issue.WRAP) {
        if (Math.random() > .66666) {
          issues.remove(Issue.WRAP);
          // TODO No longer wrapped
        }
        else {
          pkmn.takeDamage(pkmn.stats.hp.max() / 10);
          // TODO Hurt by wrap
        }
      }
      else if (current == Issue.CONFUSE) {
        if (attackself) {
          // TODO Make pokemon attack itself
          attackself = false;
        }
        else if (Math.random() > .66666) {
          issues.remove(Issue.CONFUSE);
          // TODO No longer confused
        }
      }
      else if (current == Issue.FLINCH) {
        issues.remove(Issue.FLINCH);
      }
      else if (current == Issue.FREEZE) {
        if (Math.random() > .8) {
          issues.remove(Issue.FREEZE);
          // TODO No longer frozen
        }
        else {
          // TODO Still frozen
        }
      }
      else if (current == Issue.SEEDED) {
        pkmn.takeDamage(pkmn.stats.hp.max() / 10);
        // TODO Leeched by seeds
      }
      else if (current == Issue.SEEDUSR) {
        pkmn.healDamage(pkmn.stats.hp.max() / 12);
        // TODO Recover health from leech seed
      }
      else if (current == Issue.POISON) {
        pkmn.takeDamage(pkmn.stats.hp.max() / 10);
        // TODO Hurt by poison
      }
      else if (current == Issue.SLEEP) {
        if (Math.random() > .333333) {
          issues.remove(Issue.SLEEP);
          // TODO No longer sleeping
        }
        else {
          // TODO Still sleeping
        }
      }
    }
  }

  /**
   * Cleans up all status ailments.
   */
  public void reset() {
    issues = new ArrayList<Issue>();
  }

  public boolean contains(Issue i) {
    for (Issue current : issues) {
      if (current == i) return true;
    }
    return false;
  }

  public String toString() {
    return issues.toString();
  }

  public void remove(Issue i) {
    issues.remove(i);
  }

  private ArrayList<Issue> issues = new ArrayList<Issue>();
  private Pokemon pkmn;
  private boolean awake, attackself;
}