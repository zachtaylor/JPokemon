package org.jpokemon.pokemon.move.effect;

public enum MoveEffectType {
  ATTACK, DEFENSE, SPECATTACK, SPECDEFENSE, SPEED, BURN, PARALYZE, SLEEP,
  POISON, FREEZE, CONFUSE, WRAP, FLINCH, SEED, PHYSICAL_SHIELD,
  SPECIAL_SHIELD, INVULERNABLE, HEALTH_MOD;

  public static MoveEffectType valueOf(int style) {
    return values()[style];
  }

  public boolean isStatModifier() {
    return ordinal() <= SPEED.ordinal();
  }

  public boolean isConditionModifier() {
    return ordinal() > SPEED.ordinal() && ordinal() <= FLINCH.ordinal();
  }

  public boolean isFieldModifier() {
    return ordinal() > FLINCH.ordinal() && ordinal() <= INVULERNABLE.ordinal();
  }
}