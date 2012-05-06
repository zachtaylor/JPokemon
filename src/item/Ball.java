package item;

import battle.Target;
import jpkmn.Driver;
import pokemon.*;

public class Ball extends Item {

  public Ball(int power, int quantity, String name) {
    super(power, quantity, name);
    target = Target.ENEMY;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) {
      Driver.log(Ball.class, "Not enough to use type " + getName());
      return false;
    }

    int HPmax, HPcur, BALL, STAT, q;

    HPmax = p.stats.hp.max;
    HPcur = p.stats.hp.cur;
    BALL = getPower();
    STAT = p.condition.getCatchBonus();

    q = BALL * 4 * STAT;
    q /= HPmax;
    q *= ((3 * HPmax) - (2 * HPcur));

    if (q >= 255) {
      return true;
    }
    else {
      double r = Math.sqrt(((double) q) / (255.0));
      r = Math.sqrt(r);
      r *= 65535;
      for (int i = 0; i < 4; i++) {
        if (r < Math.random() * 65535) return false;
      }
      return true;
    }
  }

}
