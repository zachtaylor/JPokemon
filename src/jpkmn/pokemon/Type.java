package jpkmn.pokemon;

public enum Type {
  NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, FIGHTING, POISON, GROUND, FLYING,
  PSYCHIC, BUG, ROCK, GHOST, DRAGON;

  /**
   * Tells if this type has an advantage against the guest
   * 
   * @param guest Type of the guest
   * @return 2.0 if yes, 1.0 if no
   */
  private double advantage(Type guest) {
    int me = this.ordinal(), you = guest.ordinal();
    double answer = 1.0;

    if (me == 1 && (you == 4 || you == 5 || you == 11))
      answer *= 2;
    else if (me == 2 && (you == 1 || you == 8 || you == 12))
      answer *= 2;
    else if (me == 3 && (you == 2 || you == 9))
      answer *= 2;
    else if (me == 4 && (you == 2 || you == 8 || you == 12))
      answer *= 2;
    else if (me == 5 && (you == 4 || you == 8 || you == 9 || you == 14))
      answer *= 2;
    else if (me == 6 && (you == 0 || you == 5 || you == 12))
      answer *= 2;
    else if (me == 7 && you == 4)
      answer *= 2;
    else if (me == 8 && (you == 1 || you == 3 || you == 7 || you == 12))
      answer *= 2;
    else if (me == 9 && (you == 4 || you == 6 || you == 11))
      answer *= 2;
    else if (me == 10 && (you == 6 || you == 7))
      answer *= 2;
    else if (me == 11 && (you == 4 || you == 10))
      answer *= 2;
    else if (me == 12 && (you == 1 || you == 5 || you == 9 || you == 11))
      answer *= 2;
    else if (me == 13 && (you == 10 || you == 13))
      answer *= 2;
    else if (me == 14 && you == 14) answer *= 2;

    // Return the answer
    return answer;
  }

  /**
   * Tells if this type is weak against the guest. 1 if true, 0 if false, and
   * -1 if the attack can't hit.
   * 
   * @param guest Type of the guest
   * @return .5 if true, 1.0 if not, 0.0 if ineffective
   */
  private double weak(Type guest) {
    int me = this.ordinal(), you = guest.ordinal();
    double answer = 1.0;

    if (me == 0 && you == 12)
      answer /= 2;
    else if (me == 0 && you == 13)
      answer *= 0.0;
    else if (me == 1 && (you == 1 || you == 2 || you == 12 || you == 14))
      answer /= 2;
    else if (me == 2 && (you == 2 || you == 4 || you == 14))
      answer /= 2;
    else if (me == 3 && (you == 3 || you == 4 || you == 14))
      answer /= 2;
    else if (me == 3 && you == 8)
      answer *= 0.0;
    else if (me == 4
        && (you == 1 || you == 4 || you == 7 || you == 9 || you == 11 || you == 14))
      answer /= 2;
    else if (me == 5 && (you == 1 || you == 2 || you == 5))
      answer /= 2;
    else if (me == 6 && (you == 7 || you == 9 || you == 10 || you == 11))
      answer /= 2;
    else if (me == 6 && you == 13)
      answer *= 0.0;
    else if (me == 7 && (you == 7 || you == 8 || you == 12 || you == 13))
      answer /= 2;
    else if (me == 8 && (you == 4 || you == 11))
      answer /= 2;
    else if (me == 8 && you == 9)
      answer *= 0.0;
    else if (me == 9 && (you == 3 || you == 12))
      answer /= 2;
    else if (me == 10 && you == 10)
      answer /= 2;
    else if (me == 11
        && (you == 1 || you == 6 || you == 7 || you == 9 || you == 13))
      answer /= 2;
    else if (me == 12 && (you == 6 || you == 8))
      answer /= 2;
    else if (me == 13 && you == 0) answer *= 0.0;

    return answer;
  }

  public double effectiveness(Type t1, Type t2) {
    double answer = advantage(t1) * weak(t1);
    if (t2 != null) answer *= advantage(t2) * weak(t2);

    return answer;
  }

  public double effectiveness(Pokemon p) {
    return effectiveness(p.type1(), p.type2());
  }

  public static Type valueOf(int r) {
    switch (r) {
    case 0:
      return NORMAL;
    case 1:
      return FIRE;
    case 2:
      return WATER;
    case 3:
      return ELECTRIC;
    case 4:
      return GRASS;
    case 5:
      return ICE;
    case 6:
      return FIGHTING;
    case 7:
      return POISON;
    case 8:
      return GROUND;
    case 9:
      return FLYING;
    case 10:
      return PSYCHIC;
    case 11:
      return BUG;
    case 12:
      return ROCK;
    case 13:
      return GHOST;
    case 14:
      return DRAGON;
    default:
      return null;
    }
  }
}
