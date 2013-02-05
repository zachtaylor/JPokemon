package org.jpokemon.battle.slot;

public enum SlotEffectType {
  SEED, PHYSICAL_SHIELD, SPECIAL_SHIELD, INVULERNABLE;

  public boolean hasFiniteDuration() {
    return this != SEED;
  }
}