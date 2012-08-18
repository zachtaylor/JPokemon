package jpkmn.game.pokemon.stat;

import jpkmn.game.base.PokemonBase;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.Pokemon;

public class StatBlock {
  public final Stat atk, stk, def, sdf, spd;
  public final Health hp; // necessary so health can do more without cast

  public StatBlock(Pokemon p) {
    _points = 0;

    hp = new Health();
    atk = new Stat();
    stk = new Stat();
    def = new Stat();
    sdf = new Stat();
    spd = new Stat();

    int level = p.level();
    hp.level(level);
    atk.level(level);
    stk.level(level);
    def.level(level);
    sdf.level(level);
    spd.level(level);

    PokemonBase base = PokemonBase.getBaseForNumber(p.number());
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
    atk.level(level);
    stk.level(level);
    def.level(level);
    sdf.level(level);
    spd.level(level);
    hp.level(level);
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

  public void effectBy(Condition.Issue i) {
    if (i == Condition.Issue.BURN) burn();
    if (i == Condition.Issue.PARALYZE) paralyze();
  }

  /**
   * Applies the status effect of getting paralyzed
   */
  private void paralyze() {
    spd._cur /= 4;
    if (spd._cur < 1) spd._cur = 1;
  }

  /**
   * Applies the status effect of getting burned
   */
  private void burn() {
    atk._cur /= 2;
    if (atk._cur < 1) atk._cur = 1;
  }

  private int _points;
}