package item;

import jpkmn.Driver;
import pokemon.*;

public class Ball extends Item {
  
  public Ball(int power, int quantity, String name) {
    super(power, quantity, name);
  }
  
  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) {
      Driver.log(Ball.class, "Not enough to use type "+name);
      return false;
    }
	
	int HPmax, HPcur, BALL, STAT, q;
	
	HPmax = p.health.max;
	HPcur = p.health.cur;
	BALL = power;
	STAT = p.status.catchBonus();
	
	q = BALL*4*STAT;
	q /= HPmax;
	q *= ((3*HPmax) - (2*HPcur));
	
	if (q >= 255) {
		return true;
	}
	else {
		double r = Math.sqrt(((double) q)/(255.0));
		r = Math.sqrt(r);
		r *= 65535;
		for (int i=0; i<4; i++) {
			if (r < Math.random() * 65535)
				return false;
		}
		return true;
	}
  }

}
