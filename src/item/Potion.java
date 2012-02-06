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
      Driver.log(Potion.class, "Not enough to use type "+name);
      return false;
    }
    
    p.healDamage(power);
    return true;
  }
}
