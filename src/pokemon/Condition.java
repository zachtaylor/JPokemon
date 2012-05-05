package pokemon;

import java.util.ArrayList;

import jpkmn.Driver;

public class Condition {

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
    if (i == Issue.WAIT) {
      issues.add(i);
    }
    else if (!issues.contains(i) && i != Issue.WAIT) {
      issues.add(i);
      gui.Tools.notify(pkmn, i.toString(), pkmn.name() + " is now " + i);

      if (i == Issue.BURN)
        pkmn.stats.atk.cur /= 2;
      else if (i == Issue.PARALYZE) pkmn.stats.spd.cur /= 4;
    }

    Driver.log(Condition.class, "New issue added to " + pkmn.name()
        + ". Effect = " + i.name());
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
    for (Issue current : issues) {
      if (current == Issue.BURN) {
        pkmn.takeDamage(pkmn.stats.hp.max / 10);
        gui.Tools.notify(pkmn, "BURN", pkmn.name() + " was hurt by it's burn!");
      }
      else if (current == Issue.WRAP) {
        if (Math.random() > .66666) {
          gui.Tools.notify(pkmn, "ESCAPE", pkmn.name()
              + " escaped from it's wrap!");
        }
        else {
          pkmn.takeDamage(pkmn.stats.hp.max / 10);
          gui.Tools.notify(pkmn, "BURN", pkmn.name()
              + " was hurt by it's wrap!");
        }
      }
      else if (current == Issue.CONFUSE) {
        if (attackself) pkmn.confusedAttack();

        if (Math.random() > .66666) {
          issues.remove(Issue.CONFUSE);
          gui.Tools.notify(pkmn, "ATTENTION", pkmn.name()
              + " is no longer confused!");
        }
      }
      else if (current == Issue.FLINCH) {
        issues.remove(Issue.FLINCH);
      }
      else if (current == Issue.FREEZE) {
        if (Math.random() > .8) {
          issues.remove(Issue.FREEZE);
          gui.Tools.notify(pkmn, "ESCAPE", pkmn.name()
              + " escaped from being frozen!");
        }
        else {
          gui.Tools.notify(pkmn, "FROZEN", pkmn.name() + " is still frozen!");
        }
      }
      else if (current == Issue.SEEDED) {
        pkmn.takeDamage(pkmn.stats.hp.max / 10);
        gui.Tools.notify(pkmn, "LEECH", pkmn.name()
            + " was leeched by the seeds!");
      }
      else if (current == Issue.SEEDUSR) {
        pkmn.healDamage(pkmn.stats.hp.max / 12);
        gui.Tools
            .notify(pkmn, "LEECH", pkmn.name() + " was healed by the seeds!");
      }
      else if (current == Issue.POISON) {
        pkmn.takeDamage(pkmn.stats.hp.max / 10);
        gui.Tools.notify(pkmn, "POISON", pkmn.name() + " was hurt by the toxin!");
      }
      else if (current == Issue.SLEEP) {
        if (Math.random() > .333333) {
          issues.remove(Issue.SLEEP);
          gui.Tools.notify(pkmn, "AWAKEN", pkmn.name() + " woke up!");
        }
        else {
          gui.Tools.notify(pkmn, "SLEEP", pkmn.name() + " is still sleeping!");
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