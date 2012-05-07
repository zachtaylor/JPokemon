package jpkmn.item;

import jpkmn.battle.Target;
import jpkmn.pokemon.*;

public class Ball extends Item {

  public Ball(int power, int quantity, String name) {
    super(power, quantity, name);
    target = Target.ENEMY;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    int HPmax, HPcur, BALL, STAT, q;

    HPmax = p.stats.hp.max;
    HPcur = p.stats.hp.cur;
    BALL = getPower();
    STAT = p.condition.getCatchBonus();

    q = BALL * 4 * STAT;
    q /= HPmax;
    q *= ((3 * HPmax) - (2 * HPcur));

    if (q >= 255)
      return true;
    else {
      double r = Math.sqrt(Math.sqrt(((double) q) / (255.0)));
      for (int i = 0; i < 4; i++) {
        if (r < Math.random()) return false;
      }
      return true;
    }
  }

}
