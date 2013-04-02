package org.jpokemon.pokemon.move;

import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.Type;
import org.jpokemon.pokemon.move.effect.MoveEffect;
import org.json.JSONException;
import org.json.JSONObject;

import com.zachtaylor.jnodalxml.XMLException;
import com.zachtaylor.jnodalxml.XMLNode;

public class Move {
  public static final String XML_NODE_NAME = "move";

  public Move(int number) {
    setNumber(number);
  }

  /**
   * Tells the number of this Move
   * 
   * @return Number used to identify this Move
   */
  public int number() {
    return _info.getNumber();
  }

  /**
   * Tells the name of this Move
   * 
   * @return What people call this Move
   */
  public String name() {
    return _info.getName();
  }

  /**
   * Tells the power of this Move, as relevant to the MoveStyle
   * 
   * @return The power of the Move, as applicable
   */
  public int power() {
    return _info.getPower();
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
    return _info.getAccuracy();
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
   * Tells the number of Turns it takes to fully execute this Move
   * 
   * @return The number of times this move must be reAdded
   */
  public int turns() {
    if (style() == MoveStyle.DELAYNEXT || style() == MoveStyle.DELAYBEFORE)
      return 2;
    // TODO : add bide

    return 1;
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
   * Fully mutate this Move to a new instance of the specified number.
   * 
   * @param number New move number
   */
  public void setNumber(int number) {
    if (number == -1) {
      _info = null;
    }
    else {
      _info = MoveInfo.get(number);
      _pp = _ppMax = _info.getPp();
      _type = Type.valueOf(_info.getType());
      _style = MoveStyle.valueOf(_info.getStyle());
    }
    _enabled = number > 0;
  }

  /**
   * Tells this Move's effectiveness against the Pokemon specified
   * 
   * @param p Pokemon targeted by this Move
   * @return Modifier for the strength of the Move due to Type
   */
  public double effectiveness(Pokemon p) {
    double answer = 1.0;

    if (p.type1() != null)
      answer *= _type.effectiveness(p.type1());
    if (p.type2() != null)
      answer *= _type.effectiveness(p.type2());

    return answer;
  }

  /**
   * Restores the PP of this Move to the maximum value, and removes any
   * disabling effects
   */
  public void restore() {
    _pp = _ppMax;
    _enabled = true;
  }

  /**
   * An atomic way to test the accuracy and decrease the PP of a move
   * 
   * @return If the move is used successfully
   */
  public boolean use() {
    boolean success = enabled() & accuracy() >= Math.random();

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
   * Applies any additional effects of this Move to the appropriate Target
   * 
   * @param user User of the move
   * @param target Victim of the move
   */
  public void applyEffects(Slot user, Slot target, int damage) {
    for (MoveEffect effect : _info.getEffects())
      effect.effect(user, target, damage);
  }

  /**
   * Tells whether the user of the move should be hurt if the move misses
   * 
   * @return True if this move penalizes the user for missing
   */
  public boolean hurtUserOnMiss() {
    return (number() == 60 || number() == 69);
  }

  /**
   * Tells whether this move will cause damage to a target
   * 
   * @return True if the target's health is lowered as a consequence of the Move
   */
  public boolean doesDamage() {
    return style() != MoveStyle.STATUS;
  }

  public boolean damageIsAbsolute() {
    // TODO : add dragon rage, bide
    return false;
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("name", name());
      data.put("pp", _pp);
      data.put("pp_max", _ppMax);
      data.put("enabled", enabled());

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    node.setAttribute("number", number() + "");
    node.setAttribute("pp", _pp + "");
    node.setAttribute("ppmax", _ppMax + "");
    node.setAttribute("enabled", _enabled + "");
    node.setSelfClosing(true);

    return node;
  }

  public void loadXML(XMLNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XMLException("Cannot read node");

    setNumber(Integer.parseInt(node.getAttribute("number")));
    _ppMax = Integer.parseInt(node.getAttribute("ppmax"));
    _pp = Integer.parseInt(node.getAttribute("pp"));
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Move))
      return false;

    Move m = (Move) o;

    return _info == m._info && _ppMax == m._ppMax;
  }

  public int hashCode() {
    return _type.ordinal() * 1375 + _style.ordinal() * 71;
  }

  private Type _type;
  private MoveInfo _info;
  private int _pp, _ppMax;
  private boolean _enabled;
  private MoveStyle _style;
}