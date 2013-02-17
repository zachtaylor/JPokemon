package org.jpokemon.pokemon;

public enum GrowthRate {
  ERRATIC, FAST, MED_FAST, MED_SLOW, SLOW, FLUCTUATING;

  public int xp(int n) {
    double xp = 0;

    switch (this) {
    case ERRATIC:
      if (n <= 50)
        xp = (100.0 - n) / 50 * n * n * n;
      else if (n <= 68)
        xp = (150.0 - n) / 100 * n * n * n;
      else if (n <= 98)
        xp = ((1911 - 10 * n) / 3) * n * n * n / 500.0;
      else
        xp = (160.0 - n) / 100 * n * n * n;
    case FAST:
      xp = 4.0 * n * n * n / 5;
    break;
    case MED_FAST:
      xp = n * n * n;
    break;
    case MED_SLOW:
      xp = 6.0 * n * n * n / 5 - 15 * n * n + 100 * n - 140;
    break;
    case SLOW:
      xp = 5.0 * n * n * n / 4;
    case FLUCTUATING:
      if (n <= 15)
        xp = ((n + 1) / 3 + 24.0) / 50 * n * n * n;
      else if (n <= 36)
        xp = (n + 14.0) / 50 * n * n * n;
      else
        xp = (n / 2 + 32.0) / 50 * n * n * n;
    }

    return Math.max(1, (int) xp);
  }
}