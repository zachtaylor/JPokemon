package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public class Ball extends Item {
  public Ball(String name, int itemID, int value, int chance) {
    super(name, itemID, value);
    _chance = chance;
    target = Target.ENEMY;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    int HPmax, HPcur, BALL, STAT, q;

    HPmax = p.stats.hp.max();
    HPcur = p.stats.hp.cur();
    BALL = _chance;
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

  private int _chance;
}