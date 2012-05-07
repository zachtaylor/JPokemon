package jpkmn.pokemon;

import jpkmn.pokemon.stat.*;
import lib.PokemonBase;


public class StatBlock {
  public final Stat atk, stk, def, sdf, spd, hp;

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
    else if (s == spd) 
      spd.usePoint();

    return true;
  }

  public void setPoints(int p) {
    points = p;
  }

  private int number, level, points;
}
