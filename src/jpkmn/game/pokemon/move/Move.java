package jpkmn.game.pokemon.move;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.Type;
import jpkmn.game.pokemon.move.effect.MoveEffect;
import jpkmn.game.pokemon.move.effect.MoveEffectInfo;

public class Move {
  public Move(int number) {
    enabled = true;
    
    MoveInfo info = MoveInfo.get(number);
    _name = info.getName();
    _power = info.getPower();
    _pp = _ppmax = info.getPp();
    _accuracy = info.getAccuracy();
    _type = Type.valueOf(info.getType());
    _style = MoveStyle.valueOf(info.getStyle());
    
    _effects =  new ArrayList<MoveEffect>();

    for (MoveEffectInfo meinfo : info.getEffects())
      _effects.add(new MoveEffect(meinfo));
  }

  public int number() {
    return _number;
  }

  public String name() {
    return _name;
  }

  public int power() {
    return _power;
  }

  public int pp() {
    return _pp;
  }

  public double accuracy() {
    return _accuracy;
  }

  public MoveStyle style() {
    return _style;
  }

  public Type type() {
    return _type;
  }

  public boolean enabled() {
    return enabled & _pp > 0;
  }

  public void enable(boolean b) {
    enabled = b;
  }

  public void restore() {
    _pp = _ppmax;
    enabled = true;
  }

  /**
   * An atomic way to test the accuracy and decrease the PP of a move
   * 
   * @return If the move is used successfully
   */
  public boolean use() {
    boolean success = enabled();

    // Move # 141 (Swift) never misses
    if (_number != 141)
      success = success & _accuracy >= Math.random();

    if (success)
      _pp--;

    return success;
  }

  public double effectiveness(Pokemon p) {
    return effectiveness(p.type1(), p.type2());
  }

  public double effectiveness(Type t1, Type t2) {
    return type().effectiveness(t1, t2);
  }

  public double STAB(Pokemon p) {
    return STAB(p.type1(), p.type2());
  }

  public double STAB(Type t1, Type t2) {
    Type type = type();
    return type == t1 || type == t2 ? 1.5 : 1.0;
  }

  public int reps() {
    if (style() != MoveStyle.REPEAT)
      return 1;

    double chance = Math.random();
    if (chance >= 0.0052083333) // 1/192
      return 5;
    else if (chance >= 0.0416666667) // 1/24
      return 4;
    else if (chance >= 0.3333333333) // 1 / 3
      return 3;
    else
      return 2; // no luck
  }

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
  private boolean enabled;
  private MoveStyle _style;
  private int _number, _power, _pp, _ppmax;

  private List<MoveEffect> _effects;
}