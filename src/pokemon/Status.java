package pokemon;

import java.util.ArrayList;
import jpkmn.Driver;

public class Status {
  private ArrayList<Effect> effects = new ArrayList<Effect>();
  private Pokemon pkmn;

  public enum Effect {
    //@preformat
    BURN, PARALYZE, SLEEP, POISON, SEEDED, WRAP, SEEDUSR, FREEZE, 
    CONFUSE, FLINCH, WAIT;
    //@format
  }

  /**
   * Creates a new Status for the specified Pokemon
   * 
   * @param p The Pokemon whom this status effects
   */
  public Status(Pokemon p) {
    pkmn = p;
  }

  /**
   * Computes the catch bonus for effects on the Pokemon. 2 if FRZ or SLP, 15
   * if BRN, PSN, or PAR. 10 normally.
   * 
   * @return the Catch Bonus
   */
  public int catchBonus() {
    if (effects.contains(Effect.FREEZE) || effects.contains(Effect.SLEEP))
      return 20;
    else if (effects.contains(Effect.BURN) || effects.contains(Effect.POISON)
        || effects.contains(Effect.PARALYZE))
      return 15;
    else
      return 10;
  }

  /**
   * Adds a new effect to the Pokemon. If already afflicted, the new one is not
   * added, except for WAITs (they can stack). If the Pokemon is BURNed or
   * PARALYZEd, stats are adjusted accordingly.
   * 
   * @param e The effect to be added
   */
  public void addEffect(Effect e) {
    if (!effects.contains(e) && e != Effect.WAIT) {
      effects.add(e);
      gui.Tools.notify(pkmn, e.toString(), pkmn.name + " is now " + e);

      if (e == Effect.BURN)
        pkmn.attack.cur /= 2;
      else if (e == Effect.PARALYZE) pkmn.speed.cur /= 4;
    }
    else if (e == Effect.WAIT) {
      effects.add(e);
    }

    Driver.log(Status.class, "New effect added to " + pkmn.name + ". Effect = "
        + e.name());
  }

  /**
   * Checks against the user's effects to see if they can attack. Returns true
   * if they can.
   * 
   * @return true if user can attack
   */
  public boolean canAttack() {
    if (effects.contains(Effect.FREEZE))
      return false;
    else if (effects.contains(Effect.SLEEP))
      return false;
    else if (effects.contains(Effect.FLINCH))
      return false;
    else if (effects.contains(Effect.PARALYZE))
      if (Math.random() < .25) {
        return false;
      }
      else {
        
      }
    else if (effects.contains(Effect.CONFUSE)) {
      if (Math.random() > .66666)
        return true;
      else {
        pkmn.confusedAttack();
        return false;
      }
    }
    return true;
  }

  /**
   * Applies effects. DOTs hurt, Flinch is removed, volatile effects have a
   * chance to dispel.
   */
  public void applyEffects() {
    for (Effect current : effects) {
      if (current == Effect.BURN) {
        pkmn.takeDamage(pkmn.health.max / 10);
        gui.Tools.notify(pkmn, "BURN", pkmn.name + " was hurt by it's burn!");
      }
      else if (current == Effect.WRAP) {
        if (Math.random() > .66666) {
          gui.Tools.notify(pkmn, "ESCAPE", pkmn.name
              + " escaped from it's wrap!");
        }
        else {
          pkmn.takeDamage(pkmn.health.max / 10);
          gui.Tools.notify(pkmn, "BURN", pkmn.name + " was hurt by it's wrap!");
        }
      }
      else if (current == Effect.CONFUSE) {
        if (Math.random() > .66666) {
          effects.remove(Effect.CONFUSE);
          gui.Tools.notify(pkmn, "ATTENTION", pkmn.name
              + " is no longer confused!");
        }
        else {
          gui.Tools
              .notify(pkmn, "CONFUSION", pkmn.name + " is still confused!");
        }
      }
      else if (current == Effect.FLINCH) {
        effects.remove(Effect.FLINCH);
      }
      else if (current == Effect.FREEZE) {
        if (Math.random() > .8) {
          effects.remove(Effect.FREEZE);
          gui.Tools.notify(pkmn, "ESCAPE", pkmn.name
              + " escaped from being frozen!");
        }
        else {
          gui.Tools.notify(pkmn, "FROZEN", pkmn.name + " is still frozen!");
        }
      }
      else if (current == Effect.SEEDED) {
        pkmn.takeDamage(pkmn.health.max / 10);
        gui.Tools.notify(pkmn, "LEECH", pkmn.name
            + " was leeched by the seeds!");
      }
      else if (current == Effect.SEEDUSR) {
        pkmn.healDamage(pkmn.health.max / 12);
        gui.Tools
            .notify(pkmn, "LEECH", pkmn.name + " was healed by the seeds!");
      }
      else if (current == Effect.POISON) {
        pkmn.takeDamage(pkmn.health.max / 10);
        gui.Tools.notify(pkmn, "POISON", pkmn.name + " was hurt by the toxin!");
      }
      else if (current == Effect.SLEEP) {
        if (Math.random() > .333333) {
          effects.remove(Effect.SLEEP);
          gui.Tools.notify(pkmn, "AWAKEN", pkmn.name + " woke up!");
        }
        else {
          gui.Tools.notify(pkmn, "SLEEP", pkmn.name + " is still sleeping!");
        }
      }
    }
  }

  /**
   * Cleans up all status ailments.
   */
  public void reset() {
    effects = new ArrayList<Effect>();
  }

  public boolean contains(Effect e) {
    for (Effect current : effects) {
      if (current == e) return true;
    }
    return false;
  }

  public String toString() {
    return effects.toString();
  }

  public void remove(Effect e) {
    effects.remove(e);
  }

  public String effectsToString() {
    return effects.toString();
  }
}
