package jpkmn.game.pokemon.move;

public enum MoveStyle {
  PHYSICAL, SPECIAL, OHKO, STATUS, REPEAT, DELAY, MISC;

  public int delay() {
    return delay;
  }

  public boolean attackAfterDelay() {
    return attackAfterDelay;
  }

  public boolean attackBeforeDelay() {
    return !attackAfterDelay;
  }

  public int getRepetitionAmount() {
    if (this != REPEAT) return -1;

    int amount = REPEAT_MIN;

    if (Math.random() > .666) {
      amount++;
      if (Math.random() > .875) {
        amount++;
        if (Math.random() > .875)
          amount++;
      }
    }

    return amount;
  }

  public static MoveStyle valueOf(int style) {
    if (style < 0 || style > values().length) return null;
    return values()[style];
  }

  private int delay;
  private boolean attackAfterDelay = true;

  private static final int REPEAT_MIN = 2;
}