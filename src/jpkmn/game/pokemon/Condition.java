package jpkmn.game.pokemon;

import java.util.ArrayList;
import java.util.List;

public class Condition {
  private int a; // flag to do work

  public enum Issue {
    BURN, PARALYZE, SLEEP, POISON, SEEDED, WRAP, SEEDUSR, FREEZE, CONFUSE,
    FLINCH, WAIT;
    ;
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
  public String[] applyEffects() {
    List<String> messages = new ArrayList<String>();

    for (Issue current : issues) {
      if (current == Issue.BURN) {
        pkmn.takeDamage(pkmn.stats.hp.max() / 10);
        messages.add(pkmn.name() + " was injured by it's burn!");
      }
      else if (current == Issue.WRAP) {
        if (Math.random() > .66666) {
          issues.remove(Issue.WRAP);
          messages.add(pkmn.name() + " freed itself!");
        }
        else {
          pkmn.takeDamage(pkmn.stats.hp.max() / 10);
          messages.add(pkmn.name() + " was injured by the binding!");
        }
      }
      else if (current == Issue.CONFUSE) {
        if (attackself) {
          // TODO Make pokemon attack itself
          attackself = false;
        }
        else if (Math.random() > .66666) {
          issues.remove(Issue.CONFUSE);
          messages.add(pkmn.name() + " is no longer confused!");
        }
      }
      else if (current == Issue.FLINCH) {
        issues.remove(Issue.FLINCH);
      }
      else if (current == Issue.FREEZE && Math.random() > .8) {
        issues.remove(Issue.FREEZE);
        messages.add(pkmn.name() + " broke out of the ice!");
      }
      else if (current == Issue.SEEDED) {
        pkmn.takeDamage(pkmn.stats.hp.max() / 10);
        messages.add(pkmn.name() + " was injured by the seeds!");
      }
      else if (current == Issue.SEEDUSR) {
        pkmn.healDamage(pkmn.stats.hp.max() / 12);
        messages.add(pkmn.name() + " recovered health!");
      }
      else if (current == Issue.POISON) {
        pkmn.takeDamage(pkmn.stats.hp.max() / 10);
        messages.add(pkmn.name() + " was injured by the poison!");
      }
      else if (current == Issue.SLEEP && Math.random() > .333333) {
        issues.remove(Issue.SLEEP);
        messages.add(pkmn.name() + " woke up!");
      }
    }

    return (String[]) messages.toArray();
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