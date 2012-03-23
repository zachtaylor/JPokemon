package pokemon;

public enum Type {
    NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DRAGON;

    /**
     * Tells if this type has an advantage against the guest
     * 
     * @param guest
     *            Type of the guest
     * @return 2.0 if yes, 1.0 if no
     */
    private double advantage(Type guest) {
        int self = this.ordinal();
        int other = guest.ordinal();
        double answer = 1.0;

        if (self == 1 && (other == 4 || other == 5 || other == 11))
            answer *= 2;
        if (self == 2 && (other == 1 || other == 8 || other == 12))
            answer *= 2;
        if (self == 3 && (other == 2 || other == 9))
            answer *= 2;
        if (self == 4 && (other == 2 || other == 8 || other == 12))
            answer *= 2;
        if (self == 5
                && (other == 4 || other == 8 || other == 9 || other == 14))
            answer *= 2;
        if (self == 6 && (other == 0 || other == 5 || other == 12))
            answer *= 2;
        if (self == 7 && other == 4)
            answer *= 2;
        if (self == 8
                && (other == 1 || other == 3 || other == 7 || other == 12))
            answer *= 2;
        if (self == 9 && (other == 4 || other == 6 || other == 11))
            answer *= 2;
        if (self == 10 && (other == 6 || other == 7))
            answer *= 2;
        if (self == 11 && (other == 4 || other == 10))
            answer *= 2;
        if (self == 12
                && (other == 1 || other == 5 || other == 9 || other == 11))
            answer *= 2;
        if (self == 13 && (other == 10 || other == 13))
            answer *= 2;
        if (self == 14 && other == 14)
            answer *= 2;

        // Return the answer
        return answer;
    }

    /**
     * Tells if this type is weak against the guest. 1 if true, 0 if false, and
     * -1 if the attack can't hit.
     * 
     * NEW: .5 if true, 1.0 if not, 0.0 if ineffective
     * 
     * @param guest
     *            Type of the guest
     * @return 1 if true. 0 if false. -1 if ineffective.
     */
    private double weak(Type guest) {
        int self = this.ordinal();
        int other = guest.ordinal();
        double answer = 1.0;

        if (self == 0 && other == 12)
            answer /= 2;
        if (self == 0 && other == 13)
            answer *= 0.0;
        if (self == 1
                && (other == 1 || other == 2 || other == 12 || other == 14))
            answer /= 2;
        if (self == 2 && (other == 2 || other == 4 || other == 14))
            answer /= 2;
        if (self == 3 && (other == 3 || other == 4 || other == 14))
            answer /= 2;
        if (self == 3 && other == 8)
            answer *= 0.0;
        if (self == 4
                && (other == 1 || other == 4 || other == 7 || other == 9
                        || other == 11 || other == 14))
            answer /= 2;
        if (self == 5 && (other == 1 || other == 2 || other == 5))
            answer /= 2;
        if (self == 6
                && (other == 7 || other == 9 || other == 10 || other == 11))
            answer /= 2;
        if (self == 6 && other == 13)
            answer *= 0.0;
        if (self == 7
                && (other == 7 || other == 8 || other == 12 || other == 13))
            answer /= 2;
        if (self == 8 && (other == 4 || other == 11))
            answer /= 2;
        if (self == 8 && other == 9)
            answer *= 0.0;
        if (self == 9 && (other == 3 || other == 12))
            answer /= 2;
        if (self == 10 && other == 10)
            answer /= 2;
        if (self == 11
                && (other == 1 || other == 6 || other == 7 || other == 9 || other == 13))
            answer /= 2;
        if (self == 12 && (other == 6 || other == 8))
            answer /= 2;
        if (self == 13 && other == 0)
            answer *= 0.0;

        return answer;
    }

    public double effectiveness(Type t1, Type t2) {
        double answer = advantage(t1) * weak(t1);
        if (t2 != null) {
            answer *= advantage(t2) * weak(t2);
        }

        return answer;
    }

    public double effectiveness(Pokemon p) {
        return effectiveness(p.type1, p.type2);
    }

    public static Type random() {
        return valueOf((int) (Math.random() * 15));
    }

    public static Type valueOf(int r) {
        switch(r) {
            case 0:  return NORMAL;
            case 1:  return FIRE;
            case 2:  return WATER;
            case 3:  return ELECTRIC;
            case 4:  return GRASS;
            case 5:  return ICE;
            case 6:  return FIGHTING;
            case 7:  return POISON;
            case 8:  return GROUND;
            case 9:  return FLYING;
            case 10: return PSYCHIC;
            case 11: return BUG;
            case 12: return ROCK;
            case 13: return GHOST;
            case 14: return DRAGON;
            default: return null;
        }
    }
}
