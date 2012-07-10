package jpkmn.game.pokemon.move;

import java.util.ArrayList;
import java.util.List;

import lib.BonusEffectBase;
import lib.MoveBase;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Type;

public class Move {
  public final Pokemon pkmn;

  /**
   * Creates a new move of the specified number
   * 
   * @param num The number of the move
   * @param user The user of the move
   */
  public Move(int num, Pokemon user) {
    _number = num;
    pkmn = user;
    _enabled = true;

    MoveBase base = MoveBase.getBaseForNumber(_number);
    _name = base.getName();
    _power = base.getPower();
    _ppcur = _ppmax = base.getPp();
    _accuracy = base.getAccuracy();
    _type = Type.valueOf(base.getType());
    _style = MoveStyle.valueOf(base.getStyle());

    setBonusEffects();
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

  public int ppcur() {
    return _ppcur;
  }

  public int ppmax() {
    return _ppmax;
  }

  /**
   * Tells whether it is valid to use this move. This method will reduce PP.
   * Note that it is not appropriate to call this method on repeat-style moves,
   * or multi-turn moves.
   * 
   * @return True if the move can be performed this turn
   */
  public boolean use() {
    return _enabled = _enabled && _ppcur-- > 0;
  }

  /**
   * Reset base attributes for a move
   */
  public void restore() {
    _ppcur = _ppmax;
    _enabled = true;
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
   * Tells the Same-Type-Attack-Bonus advantage. 1.5 is true, 1 if false
   * 
   * @return the STAB advantage
   */
  public double STAB() {
    return (_type == pkmn.type1() || _type == pkmn.type2()) ? 1.5 : 1.0;
  }

  /**
   * Figures out whether this move hits the target. Each time hits is called,
   * it is random. Therefore, do not call it multiple times per attempted
   * attack. This method only computes the probability of hitting, but does not
   * effect pp.
   * 
   * @param target Target pokemon
   * @return True if the move hits
   */
  public boolean hits(Pokemon target) {

    // Move # 141 (Swift) never misses
    if (_number == 141) return true;

    if (_style == MoveStyle.OHKO) {
      int levelDiff = pkmn.level() - target.level();

      if (levelDiff >= 0 && (levelDiff + 30.0) / 100.0 > Math.random())
        return true;
      else
        return false;
    }
    else
      return _accuracy >= Math.random();
  }

  @Override
  public String toString() {
    return _name + " (" + _ppcur + "/" + _ppmax + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Move)) return false;

    return _number == ((Move) o)._number;
  }

  public List<MoveEffect> getMoveEffects() {
    return _effects;
  }

  /**
   * Loads and sets all the bonus effects for a move
   */
  private void setBonusEffects() {
    MoveEffect current;
    _effects = new ArrayList<MoveEffect>();

    for (BonusEffectBase base : BonusEffectBase.getBasesForMoveNumber(_number)) {
      // Get Bonus Effect type
      current = MoveEffect.valueOf(base.getType());
      // Add all attributes, including unused
      current.target = Target.valueOf(base.getTarget());
      current.chance = base.getChance();
      current.percent = base.getPercent();
      current.power = base.getPower();
      // Add to moves list of effects
      _effects.add(current);
    }
  }

  private String _name;
  private int _number, _power, _ppcur, _ppmax;
  private double _accuracy;
  private boolean _enabled = true;
  private Type _type;
  private MoveStyle _style;
  private List<MoveEffect> _effects;
}