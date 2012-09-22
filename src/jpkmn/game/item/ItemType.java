package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Type;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.stat.StatType;

public enum ItemType {
  //@preformat
  BALL(Target.ENEMY), POTION(Target.SELF), XSTAT(Target.SELF),
  STONE(Target.SELF), MACHINE(Target.SELF), KEYITEM(Target.SELF);
  //@format

  ItemType(Target t) {
    _target = t;
  }

  public static ItemType valueOf(int t) {
    return ItemType.values()[t];
  }

  public Target target() {
    return _target;
  }

  public boolean effect(Pokemon p, int data) {
    switch (this) {
    case BALL:
      int HPmax = p.stats.hp.max();
      int HPcur = p.stats.hp.cur();
      int BALL = data;
      int STAT = p.condition.getCatchBonus();
      int q = BALL * 4 * STAT / HPmax;
      q *= ((3 * HPmax) - (2 * HPcur));

      if (q >= 255)
        return true;
      else {
        double r = Math.sqrt(Math.sqrt(((double) q) / (255.0)));
        for (int i = 0; i < 4; i++)
          if (r < Math.random()) return false;

        return true;
      }
    case POTION:
      p.healDamage(data);
      return true;
    case XSTAT:
      p.stats.getStat(StatType.valueOf(data)).effect(1);
      return true;
    case STONE:
      int n = p.number();
      Type t = Type.valueOf(data);

      // Eevee (#133) evolutions are not linear

      if (t == Type.FIRE) {
        if (n == 37 || n == 58)
          p.evolve();
        else if (n == 133) p.evolve(136);
      }
      else if (t == Type.WATER) {
        if (n == 60 || n == 90 || n == 120)
          p.evolve();
        else if (n == 133) p.evolve(134);
      }
      else if (t == Type.ELECTRIC) { // thunder
        if (p.number() == 25)
          p.evolve();
        else if (n == 133) p.evolve(135);
      }
      else if (t == Type.GRASS && (n == 44 || n == 70 || n == 102))
        p.evolve();
      else if (t == Type.NORMAL && (n == 30 || n == 33 || n == 35 || n == 39))
        p.evolve();

      return p.number() != n;
    case MACHINE:
      Move m = new Move(data, p);

      if ((p.type1() != m.type() && p.type2() != m.type())) return false;

      return p.moves.add(data);

    case KEYITEM:
      // TODO : useful stuff
      return false;
    }
    return false;
  }

  private Target _target;
}