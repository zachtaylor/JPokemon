package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Type;

public class Stone extends Item {
  public Stone(int type, String name, int value) {
    super(name, value);
    _type = Type.valueOf(type);
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    int n = p.number();

    // Eevee (#133) evolutions are not linear

    if (_type == Type.FIRE) {
      if (n == 37 || n == 58)
        p.changeSpecies();
      else if (n == 133) p.changeSpecies(136);
    }
    else if (_type == Type.WATER) {
      if (n == 60 || n == 90 || n == 120)
        p.changeSpecies();
      else if (n == 133) p.changeSpecies(134);
    }
    else if (_type == Type.GRASS) { // leaf
      if (n == 44 || n == 70 || n == 102) p.changeSpecies();
    }
    else if (_type == Type.ELECTRIC) { // thunder
      if (p.number() == 25)
        p.changeSpecies();
      else if (n == 133) p.changeSpecies(135);
    }
    else if (_type == Type.NORMAL) { // moon
      if (n == 30 || n == 33 || n == 35 || n == 39) p.changeSpecies();
    }

    return p.number() != n;

  }

  private Type _type;
}