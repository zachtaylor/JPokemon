package org.jpokemon.pokemon;

public enum GrowthRate {
  SLOW, MED_SLOW, MED_FAST, FAST;

  public static GrowthRate valueOf(int index) {
    return GrowthRate.values()[index];
  }

  public int xp(int n) {
    double xp = 0;

    switch (this) {
    case SLOW:
      xp = 5.0 * n * n * n / 4;
    break;
    case MED_SLOW:
      xp = 6.0 * n * n * n / 5 - 15 * n * n + 100 * n - 140;
    break;
    case MED_FAST:
      xp = n * n * n;
    break;
    case FAST:
      xp = 4.0 * n * n * n / 5;
    }

    return Math.max(1, (int) xp);
  }
}