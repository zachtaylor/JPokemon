package jpkmn.game.pokemon.stat;

import jpkmn.game.base.PokemonBase;
import jpkmn.game.pokemon.Condition;

public class StatBlock {
  public final Stat hp, atk, stk, def, sdf, spd;

  public StatBlock(PokemonBase base) {
    hp = new Health();
    atk = new Stat();
    stk = new Stat();
    def = new Stat();
    sdf = new Stat();
    spd = new Stat();

    rebase(base);
  }

  public Stat getStat(StatType type) {
    switch (type) {
    case ATTACK:
      return atk;
    case SPECATTACK:
      return stk;
    case DEFENSE:
      return def;
    case SPECDEFENSE:
      return sdf;
    case SPEED:
      return spd;
    default:
      return null;
    }
  }

  public void resetAll() {
    atk.reset();
    stk.reset();
    def.reset();
    sdf.reset();
    spd.reset();
    hp.reset();
  }

  public void resetMaxAll() {
    atk.resetMax();
    stk.resetMax();
    def.resetMax();
    sdf.resetMax();
    spd.resetMax();
    hp.resetMax();
  }

  public void level(int level) {
    hp.level(level);
    atk.level(level);
    stk.level(level);
    def.level(level);
    sdf.level(level);
    spd.level(level);
  }

  public int points() {
    return _points;
  }

  public void points(int p) {
    _points = p;
  }

  public void rebase(PokemonBase base) {
    atk.rebase(base.getAttack());
    stk.rebase(base.getSpecattack());
    def.rebase(base.getDefense());
    sdf.rebase(base.getSpecdefense());
    spd.rebase(base.getSpeed());
    hp.rebase(base.getHealth());
  }

  /**
   * Applies a stat penalty, as a result of a condition issue
   * 
   * @param i The issue which applies a stat penalty
   */
  public void effectBy(Condition.Issue i) {
    if (i == Condition.Issue.BURN) {
      _burn = true;
      atk._cur /= 2;
      if (atk._cur < 1) atk._cur = 1;
    }
    else if (i == Condition.Issue.PARALYZE) {
      _para = true;
      spd._cur /= 4;
      if (spd._cur < 1) spd._cur = 1;
    }
  }

  /**
   * Removes the previously added stat penalty of a condition issue
   * 
   * @param i The issue to reset the effects of
   */
  public void removeEffect(Condition.Issue i) {
    if (i == Condition.Issue.BURN && _burn) {
      _burn = false;
      atk.effect(0);
    }
    else if (i == Condition.Issue.PARALYZE && _para) {
      _para = false;
      spd.effect(0);
    }
  }

  private int _points;
  private boolean _burn, _para;
}