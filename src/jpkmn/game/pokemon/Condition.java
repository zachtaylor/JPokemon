package jpkmn.game.pokemon;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.battle.Battle;

public class Condition {
  public enum Issue {
    BURN, PARALYZE, SLEEP, POISON, SEEDVIC, WRAP, SEEDUSR, FREEZE, CONFUSE,
    FLINCH, WAIT;
  }

  /**
   * Creates a new Condition for the specified Pokemon
   * 
   * @param p The Pokemon whom this status effects
   */
  public Condition(Pokemon p) {
    pkmn = p;
    awake = true;
  }

  public boolean awake() {
    return awake;
  }

  public void awake(boolean b) {
    awake = b;
  }

  /**
   * Computes the catch bonus for effects on the Pokemon. 20 if FRZ or SLP, 15
   * if BRN, PSN, or PAR. 10 normally.
   * 
   * @return the Catch Bonus
   */
  public int getCatchBonus() {
    int best = 10;

    for (Issue i : issues) {
      if (i == Issue.FREEZE || i == Issue.SLEEP)
        return 20;
      else if (i == Issue.BURN || i == Issue.POISON || i == Issue.PARALYZE)
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
  public void add(Issue i) {
    if (i != Issue.WAIT && contains(i))
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
    for (Issue i : issues) {
      if (i == Issue.FREEZE || i == Issue.SLEEP || i == Issue.FLINCH)
        return false;
      else if (i == Issue.PARALYZE && Math.random() < .25)
        return false;
      else if (i == Issue.CONFUSE) {
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

    for (Issue current : issues) {
      if (current == Issue.BURN) {
        pkmn.takeDamage(pkmn.health().max() / 10);
        messages.add(pkmn.name() + " was injured by it's burn!");
      }
      else if (current == Issue.WRAP) {
        if (Math.random() > .66666) {
          remove(Issue.WRAP);
          messages.add(pkmn.name() + " freed itself!");
        }
        else {
          pkmn.takeDamage(pkmn.health().max() / 10);
          messages.add(pkmn.name() + " was injured by the binding!");
        }
      }
      else if (current == Issue.CONFUSE) {
        if (attackself) {
          pkmn.takeDamage(Battle.confusedDamage(pkmn));
          attackself = false;
        }
        else if (Math.random() > .66666) {
          remove(Issue.CONFUSE);
          messages.add(pkmn.name() + " is no longer confused!");
        }
      }
      else if (current == Issue.FLINCH) {
        remove(Issue.FLINCH);
      }
      else if (current == Issue.FREEZE && Math.random() > .8) {
        remove(Issue.FREEZE);
        messages.add(pkmn.name() + " broke out of the ice!");
      }
      else if (current == Issue.SEEDVIC) {
        pkmn.takeDamage(pkmn.health().max() / 10);
        messages.add(pkmn.name() + " was injured by the seeds!");
      }
      else if (current == Issue.SEEDUSR) {
        pkmn.healDamage(pkmn.health().max() / 12);
        messages.add(pkmn.name() + " recovered health!");
      }
      else if (current == Issue.POISON) {
        pkmn.takeDamage(pkmn.health().max() / 10);
        messages.add(pkmn.name() + " was injured by the poison!");
      }
      else if (current == Issue.SLEEP && Math.random() > .333333) {
        remove(Issue.SLEEP);
        messages.add(pkmn.name() + " woke up!");
      }
    }

    return messages.toArray(new String[messages.size()]);
  }

  /**
   * Cleans up all status ailments.
   */
  public void reset() {
    issues = new ArrayList<Issue>();
  }

  public boolean contains(Issue i) {
    return issues.contains(i);
  }

  public String toString() {
    return issues.toString();
  }

  public void remove(Issue i) {
    issues.remove(i);
  }

  private List<Issue> issues = new ArrayList<Issue>();
  private Pokemon pkmn;
  private boolean awake, attackself;
}