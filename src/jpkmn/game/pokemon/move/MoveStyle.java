package jpkmn.game.pokemon.move;

public enum MoveStyle {
    PHYSICAL, SPECIAL, OHKO, EFFECT, REPEAT, DELAY, MISC;

    public int delay;

    public boolean attackAfterDelay = true;
    
    public static final int REPEAT_MIN = 2, REPEAT_EXTRA = 3;

    public static MoveStyle valueOf(int style) {
        switch (style) {
            case 0:  return PHYSICAL;
            case 1:  return SPECIAL;
            case 2:  return OHKO;
            case 3:  return EFFECT;
            case 4:  return REPEAT;
            case 5:  return DELAY;
            default: return MISC;
        }
    }
}
