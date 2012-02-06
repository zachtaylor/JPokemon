package item;

import jpkmn.Driver;
import pokemon.*;

public class XStat extends Item {
  
  public enum Type {
    ATTACK, SATTACK, DEFENSE, SDEFENSE, SPEED;
    
    private String formatName() {
      if (this == ATTACK || this == DEFENSE || this == SPEED) {
        return name().charAt(0) + 
            name().substring(1, name().length()-1).toLowerCase();
      }
      else {
        return "Special " + 
            name().substring(1, name().length()-1).toLowerCase();
      }
    }
  }
	
  public Type type;
  
  public XStat(int power, int quantity, Type type) {
    super(power, quantity, "X"+type.formatName());
    this.type = type;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) {
      Driver.log(XStat.class, "Not enough to use type "+name);
      return false;
    }
    
	  if (type == Type.ATTACK) {
		  p.attack.effect(1);
	  }
	  else if (type == Type.SATTACK) {
		  p.specattack.effect(1);
		  
	  }
	  else if (type == Type.DEFENSE) {
		  p.defense.effect(1);
		  
	  }
	  else if (type == Type.SDEFENSE) {
      p.specdefense.effect(1);
		  
	  }
	  else {
	    p.speed.effect(1);
	    
	  }
	  
	  return true;
  }
  
}
