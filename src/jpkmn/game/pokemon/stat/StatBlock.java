package jpkmn.game.pokemon.stat;

import jpkmn.game.base.PokemonBase;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.Pokemon;

public class StatBlock {
  public final Stat atk, stk, def, sdf, spd;
  public final Health hp; // necessary so health can do more without cast

  public StatBlock(Pokemon p) {
    number = p.number();
    level = p.level();
    points = 0;

    PokemonBase base = PokemonBase.getBaseForNumber(number);
    atk = new Attack(base.getAttack(), level);
    stk = new SpecialAttack(base.getSpecattack(), level);
    def = new Defense(base.getDefense(), level);
    sdf = new SpecialDefense(base.getSpecdefense(), level);
    spd = new Speed(base.getSpeed(), level);
    hp = new Health(base.getHealth(), level);
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

  public void levelUp() {
    level++;
    points++;
    atk.setLevel(level);
    stk.setLevel(level);
    def.setLevel(level);
    sdf.setLevel(level);
    spd.setLevel(level);
    hp.setLevel(level);
  }

  public void changeSpecies(int... n) {
    if (n == null || n.length == 0) {
      number++;
      points++;
    }
    else {
      // Eevee gets points
      if (number == 133) points++;
      number = n[0];
    }

    PokemonBase base = PokemonBase.getBaseForNumber(number);
    atk.setBase(base.getAttack());
    stk.setBase(base.getSpecattack());
    def.setBase(base.getDefense());
    sdf.setBase(base.getSpecdefense());
    spd.setBase(base.getSpeed());
    hp.setBase(base.getHealth());
  }

  public int getPoints() {
    return points;
  }

  public void setPoints(int p) {
    points = p;
  }

  public boolean usePoint(Stat s) {
    if (points == 0)
      return false;
    else
      points--;

    if (s == atk)
      atk.usePoint();
    else if (s == stk)
      stk.usePoint();
    else if (s == def)
      def.usePoint();
    else if (s == sdf)
      sdf.usePoint();
    else if (s == spd) spd.usePoint();

    return true;
  }
  
  public void effectBy(Condition.Issue i) {
    if (i == Condition.Issue.BURN)
      burn();
    if (i == Condition.Issue.PARALYZE)
      paralyze();
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

  private int number, level, points;

}
