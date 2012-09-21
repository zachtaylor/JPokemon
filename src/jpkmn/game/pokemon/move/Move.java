package jpkmn.game.pokemon.move;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.base.BonusEffectBase;
import jpkmn.game.base.MoveBase;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Type;

public class Move {
  /**
   * Creates a new move of the specified number
   * 
   * @param num The number of the move
   * @param user The user of the move
   */
  public Move(int num, Pokemon user) {
    _number = num;
    _pokemon = user;
    _enabled = true;

    MoveBase mb = MoveBase.get(_number);
    _name = mb.getName();
    _power = mb.getPower();
    _pp = _ppmax = mb.getPp();
    _accuracy = mb.getAccuracy();
    _type = Type.valueOf(mb.getType());
    _style = MoveStyle.valueOf(mb.getStyle());

    _effects = new ArrayList<MoveEffect>();

    List<BonusEffectBase> bebs = BonusEffectBase.getBasesForMoveNumber(_number);
    for (BonusEffectBase beb : bebs)
      _effects.add(new MoveEffect(beb));
  }

  public String name() {
    return _name;
  }

  public int number() {
    return _number;
  }

  public int power() {
    return _power;
  }

  public MoveStyle style() {
    return _style;
  }

  public Type type() {
    return _type;
  }

  public int pp() {
    return _pp;
  }

  public void pp(int pp) {
    _pp = pp;

    if (_pp <= 0) {
      _pp = 0;
      _enabled = false;
    }
    else {
      if (_pp > _ppmax) _pp = _ppmax;
      _enabled = true;
    }
  }

  public int ppmax() {
    return _ppmax;
  }

  public boolean enabled() {
    return _enabled;
  }

  public void enabled(boolean enabled) {
    _enabled = enabled;
  }

  public List<MoveEffect> moveEffects() {
    return _effects;
  }

  /**
   * Tells the Same-Type-Attack-Bonus advantage. 1.5 is true, 1 if false
   * 
   * @return the STAB advantage
   */
  public double STAB() {
    return (_type == _pokemon.type1() || _type == _pokemon.type2()) ? 1.5 : 1.0;
  }

  /**
   * Determine whether a move is normal, super, or not very effective
   * 
   * @param p The pokemon targeted by the move
   * @return How effect the move is
   */
  public double effectiveness(Pokemon p) {
    return _type.effectiveness(p);
  }

  /**
   * Figures out whether this move hits the target. Each time hits is called,
   * it is random. Therefore, do not call it multiple times per attempted
   * attack.
   * 
   * @param target Target pokemon
   * @return True if the move hits
   */
  public boolean hits(Pokemon target) {

    // Move # 141 (Swift) never misses
    if (_number == 141) return true;

    if (_style == MoveStyle.OHKO) {
      int levelDiff = _pokemon.level() - target.level();

      if (levelDiff >= 0 && (levelDiff + 30.0) / 100.0 > Math.random())
        return true;
      else
        return false;
    }
    else
      return _accuracy >= Math.random();
  }

  public String toString() {
    return _name + " (" + _pp + "/" + _ppmax + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Move)) return false;

    return _number == ((Move) o)._number;
  }

  private Type _type;
  private String _name;
  private double _accuracy;
  private Pokemon _pokemon;
  private boolean _enabled;
  private MoveStyle _style;
  private List<MoveEffect> _effects;
  private int _number, _power, _pp, _ppmax;
}