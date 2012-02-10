package item;

import jpkmn.Driver;
import pokemon.*;

public class Potion extends Item {
  
  public Potion(int power, int quantity, String name) {
    super(power, quantity, name);
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
