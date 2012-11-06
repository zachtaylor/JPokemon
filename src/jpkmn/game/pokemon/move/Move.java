package jpkmn.game.pokemon.move;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Type;
import jpkmn.game.pokemon.move.effect.MoveEffect;
import jpkmn.game.pokemon.move.effect.MoveEffectInfo;

public class Move {
  public Move(int number) {
    _enabled = true;
    _number = number;

    MoveInfo info = MoveInfo.get(number);
    _name = info.getName();
    _power = info.getPower();
    _pp = _ppmax = info.getPp();
    _accuracy = info.getAccuracy();
    _type = Type.valueOf(info.getType());
    _style = MoveStyle.valueOf(info.getStyle());

    _effects = new ArrayList<MoveEffect>();

    for (MoveEffectInfo meinfo : info.getEffects())
      _effects.add(new MoveEffect(meinfo));
  }

  /**
   * Tells the number of this Move
   * 
   * @return Number used to identify this Move
   */
  public int number() {
    return _number;
  }

  /**
   * Tells the name of this Move
   * 
   * @return What people call this Move
   */
  public String name() {
    return _name;
  }

  /**
   * Tells the power of this Move, as relevant to the MoveStyle
   * 
   * @return The power of the Move, as applicable
   */
  public int power() {
    return _power;
  }

  /**
   * Tells the current available pp of this Move
   * 
   * @return
   */
  public int pp() {
    return _pp;
  }

  /**
   * Tells the accuracy of this Move
   * 
   * @return Number between 0 and 1 which represents the chance that this move
   *         will be effectively executed
   */
  public double accuracy() {
    return _accuracy;
  }

  /**
   * Tells the MoveStyle of this Move
   * 
   * @return This Move's MoveStyle
   */
  public MoveStyle style() {
    return _style;
  }

  /**
   * Tells the Type of this Move
   * 
   * @return This Move's Type
   */
  public Type type() {
    return _type;
  }

  /**
   * Tells whether this Move is enabled, and has sufficient PP to be used
   * 
   * @return If this move can be used
   */
  public boolean enabled() {
    return _enabled & _pp > 0;
  }

  /**
   * Sets the enabled status of the Move, independent of any available PP
   * 
   * @param b New usability status of this Move
   */
  public void enable(boolean b) {
    _enabled = b;
  }

  /**
   * Restores the PP of this Move to the maximum value, and removes any
   * disabling effects
   */
  public void restore() {
    _pp = _ppmax;
    _enabled = true;
  }

  /**
   * An atomic way to test the accuracy and decrease the PP of a move
   * 
   * @return If the move is used successfully
   */
  public boolean use() {
    boolean success = enabled() & _accuracy >= Math.random();

    if (success)
      _pp--;

    return success;
  }

  /**
   * Tells the Same Type Attack Bonus of the Move.<br />
   * <br />
   * This is 1.5 if the move is compatible with the given types, or 1.0 if not
   * 
   * @param p Pokemon to check the types of, for a match
   * @return Modifier for the strength of the move, given the possibly matched
   *         types
   */
  public double STAB(Pokemon p) {
    return STAB(p.type1(), p.type2());
  }

  /**
   * Tells the Same Type Attack Bonus of the Move.<br />
   * <br />
   * This is 1.5 if the move is compatible with the given types, or 1.0 if not
   * 
   * @param t1 First type which may match
   * @param t2 Second type which may match
   * @return Modifier for the strength of the move, given the possibly matched
   *         types
   */
  public double STAB(Type t1, Type t2) {
    Type type = type();
    return type == t1 || type == t2 ? 1.5 : 1.0;
  }

  /**
   * Tells the number of random number of repetitions a move has, if applicable
   * 
   * @return Number of times this move strikes simultaneously
   */
  public int reps() {
    if (style() != MoveStyle.REPEAT)
      return 1;

    double chance = Math.random();
    if (chance >= 0.9947916667) // 1/192
      return 5;
    else if (chance >= 0.9583333333) // 1/24
      return 4;
    else if (chance >= 0.6666666667) // 1 / 3
      return 3;
    else
      return 2; // no luck
  }

  /**
   * Applies any additional effects of this move to the appropriate Target
   * 
   * @param user User of the move
   * @param enemy Victim of the move
   */
  public void applyEffects(Pokemon user, Pokemon enemy) {
    for (MoveEffect effect : _effects)
      effect.effect(user, enemy);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Move))
      return false;

    Move m = (Move) o;

    return _number == m._number;
  }

  public int hashCode() {
    return _number;
  }

  private Type _type;
  private String _name;
  private double _accuracy;
  private boolean _enabled;
  private MoveStyle _style;
  private int _number, _power, _pp, _ppmax;

  private List<MoveEffect> _effects;
}