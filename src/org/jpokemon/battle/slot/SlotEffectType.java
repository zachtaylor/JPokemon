package org.jpokemon.battle.slot;

import org.zachtaylor.myna.Myna;

public enum SlotEffectType {
  SEED, PHYSICAL_SHIELD, SPECIAL_SHIELD, INVULERNABLE;

  public static int shieldturns = 5;

  static {
    Myna.configure(SlotEffectType.class, "org.jpokemon.battle");
  }

  public boolean hasFiniteDuration() {
    return this != SEED;
  }

}