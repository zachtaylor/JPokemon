package org.jpokemon.item;

import org.jpokemon.battle.Target;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.Type;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.stat.StatType;

public enum ItemType {
  BALL, POTION, XSTAT, STONE, MACHINE, KEYITEM;

  public static ItemType valueOf(int t) {
    return ItemType.values()[t];
  }

  public Target target() {
    switch (this) {
    case POTION:
    case XSTAT:
    case STONE:
    case MACHINE:
    case KEYITEM:
      return Target.SELF;
    case BALL:
      return Target.ENEMY;
    default:
      return null;
    }
  }

  public boolean effect(Pokemon pokemon, ItemInfo info) {
    switch (this) {
    case BALL:
      int a = (int) (Math.random() * info.getData()) + 1;
      a *= (3 * pokemon.maxHealth() - 2 * pokemon.health()) * pokemon.catchRate();
      a /= 30 * pokemon.maxHealth();
      a *= pokemon.catchBonus();

      return a >= 255;
    case POTION:
      pokemon.healDamage(info.getData());
      return true;
    case XSTAT:
      pokemon.getStat(StatType.valueOf(info.getData())).effect(1);
      return true;
    case STONE:
      int n = pokemon.number();

      // Eevee (#133) evolutions are not linear
      switch (Type.valueOf(info.getData())) {
      case FIRE:
        if (n == 37 || n == 58)
          pokemon.evolve();
        else if (n == 133)
          pokemon.evolve(136);
      break;
      case WATER:
        if (n == 60 || n == 90 || n == 120)
          pokemon.evolve();
        else if (n == 133)
          pokemon.evolve(134);
      break;
      case ELECTRIC:
        if (pokemon.number() == 25)
          pokemon.evolve();
        else if (n == 133)
          pokemon.evolve(135);
      break;
      case GRASS:
        if (n == 44 || n == 70 || n == 102)
          pokemon.evolve();
      break;
      case NORMAL:
        if (n == 30 || n == 33 || n == 35 || n == 39)
          pokemon.evolve();
      break;
      default:
      break;
      }
      return pokemon.number() != n;
    case MACHINE:
      if (new Move(info.getData()).STAB(pokemon) == 1)
        return false;

      try {
        pokemon.addMove(info.getData());
      } catch (IllegalStateException e) {
        ; // TODO : calculate position and ask
      }
      return false;
    case KEYITEM:
      // TODO : useful stuff
    default:
      return false;
    }
  }
}