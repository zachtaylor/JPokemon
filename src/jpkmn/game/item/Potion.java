package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public class Potion extends Item {

  public Potion(int power, int quantity, String name) {
    super(power, quantity, name);
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    p.healDamage(getPower());
    return true;
  }
}
