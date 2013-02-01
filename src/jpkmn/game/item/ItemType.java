package jpkmn.game.item;

import jpkmn.game.battle.Target;

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

  public boolean effect(Pokemon p, ItemInfo info) {
    switch (this) {
    case BALL:
      double STAT = p.catchBonus();
      int HPmax = p.maxHealth(),
      HPcur = p.health(),
      BALL = info.getData(),
      q = (int) (BALL * 40 * STAT / HPmax * ((3 * HPmax) - (2 * HPcur)));

      if (q >= 255)
        return true;
      else {
        double r = Math.sqrt(Math.sqrt(((double) q) / (255.0)));
        for (int i = 0; i < 4; i++)
          if (r < Math.random())
            return false;

        return true;
      }
    case POTION:
      p.healDamage(info.getData());
      return true;
    case XSTAT:
      p.getStat(StatType.valueOf(info.getData())).effect(1);
      return true;
    case STONE:
      int n = p.number();

      // Eevee (#133) evolutions are not linear
      switch (Type.valueOf(info.getData())) {
      case FIRE:
        if (n == 37 || n == 58)
          p.evolve();
        else if (n == 133)
          p.evolve(136);
        break;
      case WATER:
        if (n == 60 || n == 90 || n == 120)
          p.evolve();
        else if (n == 133)
          p.evolve(134);
        break;
      case ELECTRIC:
        if (p.number() == 25)
          p.evolve();
        else if (n == 133)
          p.evolve(135);
        break;
      case GRASS:
        if (n == 44 || n == 70 || n == 102)
          p.evolve();
        break;
      case NORMAL:
        if (n == 30 || n == 33 || n == 35 || n == 39)
          p.evolve();
        break;
      default:
        break;
      }
      return p.number() != n;
    case MACHINE:
      if (new Move(info.getData()).STAB(p) == 1)
        return false;

      try {
        p.addMove(info.getData());
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