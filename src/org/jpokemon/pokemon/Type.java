package org.jpokemon.pokemon;

public enum Type {
  NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, FIGHTING, POISON, GROUND, FLYING,
  PSYCHIC, BUG, ROCK, GHOST, DRAGON;

  public static Type valueOf(int r) {
    if (r < 0 || r > Type.values().length)
      return null;

    return Type.values()[r];
  }

  /**
   * Tells this Type's effectiveness against another Type
   * 
   * @param t Type to test
   * @return A modifier for move of this type's strength
   */
  @SuppressWarnings("incomplete-switch")
  public Effectiveness effectiveness(Type t) {
    switch (this) {
    case NORMAL:
      switch (t) {
      case ROCK:
        return Effectiveness.NOT_VERY;
      case GHOST:
        return Effectiveness.IMMUNE;
      }
      break;
    case FIRE:
      switch (t) {
      case GRASS: case ICE: case BUG:
        return Effectiveness.SUPER;
      case FIRE: case WATER: case ROCK: case DRAGON:
        return Effectiveness.NOT_VERY;
      }
      break;
    case WATER:
      switch (t) {
      case FIRE: case GROUND: case ROCK:
        return Effectiveness.SUPER;
      case WATER: case GRASS: case DRAGON:
        return Effectiveness.NOT_VERY;
      }
      break;
    case ELECTRIC:
      switch (t) {
      case WATER: case FLYING:
        return Effectiveness.SUPER;
      case ELECTRIC: case GRASS: case DRAGON:
        return Effectiveness.NOT_VERY;
      case GROUND:
        return Effectiveness.IMMUNE;
      }
      break;
    case GRASS:
      switch (t) {
      case WATER: case GROUND: case ROCK:
        return Effectiveness.SUPER;
      case FIRE: case GRASS: case POISON: case FLYING: case BUG: case DRAGON:
        return Effectiveness.NOT_VERY;
      }
      break;
    case ICE:
      switch (t) {
      case GRASS: case GROUND: case FLYING: case DRAGON:
        return Effectiveness.SUPER;
      case FIRE: case WATER: case ICE:
        return Effectiveness.NOT_VERY;
      }
      break;
    case FIGHTING:
      switch (t) {
      case NORMAL: case ICE: case ROCK:
        return Effectiveness.SUPER;
      case POISON: case FLYING: case PSYCHIC: case BUG:
        return Effectiveness.NOT_VERY;
      case GHOST:
        return Effectiveness.IMMUNE;
      }
      break;
    case POISON:
      switch (t) {
      case GRASS:
        return Effectiveness.SUPER;
      case POISON: case GROUND: case ROCK: case GHOST:
        return Effectiveness.NOT_VERY;
      }
      break;
    case GROUND:
      switch (t) {
      case FIRE: case ELECTRIC: case POISON: case ROCK:
        return Effectiveness.SUPER;
      case GRASS: case BUG:
        return Effectiveness.NOT_VERY;
      case FLYING:
        return Effectiveness.IMMUNE;
      }
      break;
    case FLYING:
      switch (t) {
      case GRASS: case FIGHTING: case BUG:
        return Effectiveness.SUPER;
      case ELECTRIC: case ROCK:
        return Effectiveness.NOT_VERY;
      }
      break;
    case PSYCHIC:
      switch (t) {
      case FIGHTING: case POISON:
        return Effectiveness.SUPER;
      case PSYCHIC:
        return Effectiveness.NOT_VERY;
      }
      break;
    case BUG:
      switch (t) {
      case GRASS: case PSYCHIC:
        return Effectiveness.SUPER;
      case FIRE: case FIGHTING: case POISON: case FLYING: case GHOST:
        return Effectiveness.NOT_VERY;
      }
      break;
    case ROCK:
      switch (t) {
      case FIRE: case ICE: case FLYING: case BUG:
        return Effectiveness.SUPER;
      case FIGHTING: case GROUND:
        return Effectiveness.NOT_VERY;
      }
      break;
    case GHOST:
      switch (t) {
      case PSYCHIC: case GHOST:
        return Effectiveness.SUPER;
      case NORMAL:
        return Effectiveness.IMMUNE;
      }
      break;
    case DRAGON:
      if (t == DRAGON)
        return Effectiveness.SUPER;
      break;
    }

    return Effectiveness.NORMAL;
  }

  public enum Effectiveness {
    IMMUNE, NOT_VERY, NORMAL, SUPER;
  }
}