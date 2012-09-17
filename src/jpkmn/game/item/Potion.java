package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public class Potion extends Item {

  public Potion(String name, int itemID, int value, int power) {
    super(name, itemID, value);
    _power = power;
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    p.healDamage(_power);
    return true;
  }

  private int _power;
}