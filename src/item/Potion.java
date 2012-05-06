package item;

import battle.Target;
import jpkmn.Driver;
import pokemon.*;

public class Potion extends Item {
  
  public Potion(int power, int quantity, String name) {
    super(power, quantity, name);
    target = Target.SELF;
  }
  
  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) {
      Driver.log(Potion.class, "Not enough to use type " + getName());
      return false;
    }
    
    p.healDamage(getPower());
    return true;
  }
}
