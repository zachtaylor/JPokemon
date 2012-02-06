package pokemon.move;

public enum MoveStyle {
    PHYSICAL, SPECIAL, OHKO, EFFECT, REPEAT, DELAY, MISC;

    public int repeatmin, repeatabovemin, delay;

    public boolean attackAfterDelay = true;

    public static MoveStyle valueOf(int style) {
        if (style == -1)
            return null;
        else if (style == 0)
            return PHYSICAL;
        else if (style == 1)
            return SPECIAL;
        else if (style == 2)
            return OHKO;
        else if (style == 3)
            return EFFECT;
        else if (style == 4)
            return REPEAT;
        else if (style == 5)
            return DELAY;
        else
            return MISC;
    }

}
