package org.jpokemon.pokemon;

import org.jpokemon.JPokemonConstants;

public enum Type implements JPokemonConstants {
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
  public double effectiveness(Type t) {
    switch (this) {
    case NORMAL:
      switch (t) {
      case ROCK:
        return TYPE_DISADVANTAGE_MODIFIER;
      case GHOST:
        return 0;
      }
      break;
    case FIRE:
      switch (t) {
      case GRASS: case ICE: case BUG:
        return TYPE_ADVANTAGE_MODIFIER;
      case FIRE: case WATER: case ROCK: case DRAGON:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case WATER:
      switch (t) {
      case FIRE: case GROUND: case ROCK:
        return TYPE_ADVANTAGE_MODIFIER;
      case WATER: case GRASS: case DRAGON:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case ELECTRIC:
      switch (t) {
      case WATER: case FLYING:
        return TYPE_ADVANTAGE_MODIFIER;
      case ELECTRIC: case GRASS: case DRAGON:
        return TYPE_DISADVANTAGE_MODIFIER;
      case GROUND:
        return 0;
      }
      break;
    case GRASS:
      switch (t) {
      case WATER: case GROUND: case ROCK:
        return TYPE_ADVANTAGE_MODIFIER;
      case FIRE: case GRASS: case POISON: case FLYING: case BUG: case DRAGON:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case ICE:
      switch (t) {
      case GRASS: case GROUND: case FLYING: case DRAGON:
        return TYPE_ADVANTAGE_MODIFIER;
      case FIRE: case WATER: case ICE:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case FIGHTING:
      switch (t) {
      case NORMAL: case ICE: case ROCK:
        return TYPE_ADVANTAGE_MODIFIER;
      case POISON: case FLYING: case PSYCHIC: case BUG:
        return TYPE_DISADVANTAGE_MODIFIER;
      case GHOST:
        return 0;
      }
      break;
    case POISON:
      switch (t) {
      case GRASS:
        return TYPE_ADVANTAGE_MODIFIER;
      case POISON: case GROUND: case ROCK: case GHOST:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case GROUND:
      switch (t) {
      case FIRE: case ELECTRIC: case POISON: case ROCK:
        return TYPE_ADVANTAGE_MODIFIER;
      case GRASS: case BUG:
        return TYPE_DISADVANTAGE_MODIFIER;
      case FLYING:
        return 0;
      }
      break;
    case FLYING:
      switch (t) {
      case GRASS: case FIGHTING: case BUG:
        return TYPE_ADVANTAGE_MODIFIER;
      case ELECTRIC: case ROCK:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case PSYCHIC:
      switch (t) {
      case FIGHTING: case POISON:
        return TYPE_ADVANTAGE_MODIFIER;
      case PSYCHIC:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case BUG:
      switch (t) {
      case GRASS: case PSYCHIC:
        return TYPE_ADVANTAGE_MODIFIER;
      case FIRE: case FIGHTING: case POISON: case FLYING: case GHOST:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case ROCK:
      switch (t) {
      case FIRE: case ICE: case FLYING: case BUG:
        return TYPE_ADVANTAGE_MODIFIER;
      case FIGHTING: case GROUND:
        return TYPE_DISADVANTAGE_MODIFIER;
      }
      break;
    case GHOST:
      switch (t) {
      case PSYCHIC: case GHOST:
        return TYPE_ADVANTAGE_MODIFIER;
      case NORMAL:
        return 0;
      }
      break;
    case DRAGON:
      if (t == DRAGON)
        return TYPE_ADVANTAGE_MODIFIER;
      break;
    }

    return 1.0;
  }
}