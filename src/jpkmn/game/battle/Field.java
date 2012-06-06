package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;

public class Field {
  public Field(Slot s) {
    _slot = s;
    _effects = new ArrayList<Effect>();
    _shields = new ArrayList<Shield>();
  }

  /**
   * Adds an Effect to this Field. Use this version for simple effects.
   * 
   * @param e Field.Effect to be added
   * @param d Duration of the effect in turns
   * @param exceptions Names of attacks that are exempt from invulnerable
   */
  public void add(Effect e, int duration, String... exceptions) {
    add(e, duration, 0.0, exceptions);
  }

  /**
   * Adds an Effect to this Field. Use this version for Shields.
   * 
   * @param e Field.Effect to be added
   * @param d Duration of the effect in turns
   * @param s Value between 0 and 1 representing damage reduction
   * @param exceptions Names of attacks that are exempt from invulnerable
   */
  public void add(Effect e, int d, double s, String... exceptions) {
    // TODO Make it respect shields, and handle seeding

  }

  public void effect(Turn turn) {
    applyShielding(turn);
    // APPLY WEATHER CHANGES HERE WHEN APPLICABLE
  }

  public void rollDownDuration() {
    for (int i = 0; i < _shields.size(); i++)
      if (!_shields.get(i).reduceDuration()) _shields.remove(i--);
  }

  private void applyShielding(Turn turn) {
    // TODO reduce the damage;
  }

  public boolean contains(Effect e) {
    return _effects.contains(e);
  }

  public enum Effect {
    SEEDS, SEEDED, INVULNERABLE, SPECSHIELD, PHYSSHIELD;
  }

  private Slot _slot;
  private List<Effect> _effects;
  private List<Shield> _shields;
}
