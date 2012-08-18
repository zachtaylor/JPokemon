package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public class Potion extends Item {

  public Potion(int power, String name, int value) {
    super(name, value);
    _power = power;
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    p.healDamage(_power);
    return true;
  }

  int _power;
}
