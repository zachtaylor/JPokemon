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
    
    public static MoveStyle valueOf(int style) {
        switch (style) {
            case 0:  return PHYSICAL;
            case 1:  return SPECIAL;
            case 2:  return OHKO;
            case 3:  return STATUS;
            case 4:  return REPEAT;
            case 5:  return DELAY;
            default: return MISC;
        }
    }

    private int delay;
    private boolean attackAfterDelay = true;
    private static final int REPEAT_MIN = 2, REPEAT_EXTRA = 3;
}
