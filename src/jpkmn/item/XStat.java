package jpkmn.item;

import jpkmn.Driver;
import jpkmn.battle.Target;
import jpkmn.pokemon.*;

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
	
  private Type type;
  
  public XStat(int power, int quantity, Type type) {
    super(power, quantity, "X"+type.formatName());
    this.type = type;
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) {
      Driver.log(XStat.class, "Not enough to use type " + getName());
      return false;
    }
    
	  if (type == Type.ATTACK) {
		  p.stats.atk.effect(1);
	  }
	  else if (type == Type.SATTACK) {
		  p.stats.stk.effect(1);
		  
	  }
	  else if (type == Type.DEFENSE) {
		  p.stats.def.effect(1);
		  
	  }
	  else if (type == Type.SDEFENSE) {
      p.stats.sdf.effect(1);
		  
	  }
	  else {
	    p.stats.spd.effect(1);
	  }
	  
	  return true;
  }
  
}
