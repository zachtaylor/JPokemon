package battle;

import java.util.ArrayList;
import pokemon.Status;
import jpkmn.Driver;

public class Field {

  public enum Effect {
    SEEDS, SEEDED, INVULNERABLE, SPECSHIELD, PHYSSHIELD;

    public int duration;
    public double shielding;
    
    public ArrayList<String> exceptions;
  }

  private ArrayList<Effect> effects;
  private Slot slot;

  public Field(Slot s) {
    effects = new ArrayList<Effect>();
    slot = s;
  }

  /**
   * Adds an Effect to this Field. Use this version for simple effects.
   * @param e Field.Effect to be added
   * @param d Duration of the effect in turns
   * @param exceptions Names of attacks that are exempt from invulnerable
   */
  public void add(Effect e, int duration, String... exceptions) {
    add(e, duration, 0.0, exceptions);
  }

  /**
   * Adds an Effect to this Field. Use this version for Shields.
   * @param e Field.Effect to be added
   * @param d Duration of the effect in turns
   * @param s Value between 0 and 1 representing damage reduction
   * @param exceptions Names of attacks that are exempt from invulnerable
   */
  public void add(Effect e, int d, double s, String... exceptions) {
    Driver.log(Field.class, "Adding effect : " + e.name());
    if (effects.contains(e))
      effects.remove(e);
    if (exceptions.length > 0) {
      e.exceptions = new ArrayList<String>();
      for (String string : exceptions)
        e.exceptions.add(string);
    }
    e.duration = d;
    e.shielding = s;
    effects.add(e);
  }
  
  /**
   * Makes sure that the slot leader is properly seeded/seeduser
   */
  public void resetEffects() {
    verifyEffects();
    for (Effect e : effects) {
      if (e == Effect.SEEDS) {
        slot.leader.status.addEffect(Status.Effect.SEEDUSR);
      }
      else if (e == Effect.SEEDED) {
        slot.leader.status.addEffect(Status.Effect.SEEDED);
      }
      // Invulnerable cannot persist between Pokemon, so no need to reset it
      // Phys and Spec Shield are Field-specific. don't apply to Pokemon.
    }
  }
  
  private void verifyEffects() {
    for (Effect e : effects) {
      if (e.duration <= 0)
        effects.remove(e);
    }
  }
  
  public void rollDownDuration() {
    for (Effect e : effects) {
      // Seed status doesn't ever wear off
      if (e.ordinal() > Effect.SEEDED.ordinal())
        e.duration -= 1;
    }
    verifyEffects();
  }
  
  public boolean contains(Effect e) {
    return effects.contains(e);
  }
  
  /**
   * Reports whether immunity is given from the move name.
   * @param s Name of the move
   * @return True if immunity is given
   */
  public boolean isImmune(String s) {
    for (Effect e : effects) {
      if (e == Effect.INVULNERABLE &&
          !e.exceptions.contains(s))
        return true;
    }
    return false;
  }
}
