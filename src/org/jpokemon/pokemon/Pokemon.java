package org.jpokemon.pokemon;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveBlock;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.stat.StatBlock;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.trainer.TrainerState;
import org.json.JSONException;
import org.json.JSONObject;

import com.zachtaylor.jnodalxml.XMLException;
import com.zachtaylor.jnodalxml.XMLNode;

public class Pokemon {
  public static final String XML_NODE_NAME = "pokemon";

  public Pokemon(int num) {
    _number = num;
    _condition = new Condition();
    _moves = new MoveBlock(_number);
    _species = PokemonInfo.get(_number);
    _stats = new StatBlock(_species);
  }

  public Pokemon(int num, int lvl) {
    this(num);
    _level = lvl;
    _stats.level(lvl);
    _moves.randomize(lvl);
  }

  public int number() {
    return _number;
  }

  public String name() {
    if (_name == null)
      return species();

    return _name;
  }

  public void name(String s) {
    _name = s;
  }

  public String species() {
    return _species.getName();
  }

  public int level() {
    return _level;
  }

  public int evolutionLevel() {
    return _species.getEvolutionlevel();
  }

  public void level(int l) {
    if (_level + 1 == l)
      _stats.points(_stats.points() + JPokemonConstants.STAT_POINTS_PER_LEVEL);

    _level = l;
    _stats.level(l);

    checkNewMoves();

    _condition.reset();
  }

  public Type type1() {
    return Type.valueOf(_species.getType1());
  }

  public Type type2() {
    return Type.valueOf(_species.getType2());
  }

  public String getTrainerName() {
    return _ot;
  }

  public void setTrainerName(String s) {
    if (_ot != null)
      _hasOriginalTrainer = false;
    else
      _ot = s;
  }

  public boolean hasOriginalTrainer() {
    return _hasOriginalTrainer;
  }

  public int xp() {
    return _xp;
  }

  /**
   * Adds the xp specified to the Pokemon. If the Pokemon has enough, level is
   * increased
   * 
   * @param amount Amount of xp to add
   */
  public void xp(int amount) {
    int xpNeeded = xpNeeded();

    _xp += amount;
    if (_xp >= xpNeeded) {
      _xp -= xpNeeded;
      level(level() + 1);
    }
  }

  /**
   * XP this Pokemon needs to level
   * 
   * @return The amount of XP needed to gain a level
   */
  public int xpNeeded() {
    GrowthRate rate = GrowthRate.valueOf(_species.getGrowthrate());

    return rate.xp(level());
  }

  public int xpYield() {
    return _species.getXpyield();
  }

  public Stat getStat(StatType s) {
    return _stats.get(s);
  }

  public void statPoints(StatType s, int amount) {
    _stats.usePoints(s, amount);
  }

  public int health() {
    int val = getStat(StatType.HEALTH).cur();
    return val;
  }

  public int maxHealth() {
    return getStat(StatType.HEALTH).max();
  }

  public int attack() {
    return getStat(StatType.ATTACK).cur();
  }

  public int specattack() {
    return getStat(StatType.SPECATTACK).cur();
  }

  public int defense() {
    return getStat(StatType.DEFENSE).cur();
  }

  public int specdefense() {
    return getStat(StatType.SPECDEFENSE).cur();
  }

  public int speed() {
    return getStat(StatType.SPEED).cur();
  }

  public List<EffortValue> effortValues() {
    return _species.getEffortValues();
  }

  public void addEV(List<EffortValue> evs) {
    _stats.addEV(evs);
  }

  public Move move(int index) {
    return _moves.get(index);
  }

  public void addMove(int number) {
    _moves.add(number);
  }

  public int moveCount() {
    return _moves.count();
  }

  /**
   * Takes a specified amount of damage. If damage is greater than available
   * health, the Pokemon is knocked out.
   * 
   * @param damage The amount of damage to be taken
   * @return the awake state of the Pokemon
   */
  public void takeDamage(int damage) {
    getStat(StatType.HEALTH).effect(-damage);
  }

  /**
   * Heals specified damage. If healed amount is greater than missing health,
   * Pokemon is brought to full health.
   * 
   * @param heal The amount healed by
   */
  public void healDamage(int heal) {
    getStat(StatType.HEALTH).effect(heal);
  }

  public boolean awake() {
    return health() > 0;
  }

  public String condition() {
    return _condition.toString();
  }

  public void addConditionEffect(ConditionEffect e) {
    _stats.addConditionEffect(e);
    _condition.add(e);
  }

  public boolean hasConditionEffect(ConditionEffect e) {
    return _condition.contains(e);
  }

  public boolean removeConditionEffect(ConditionEffect e) {
    _stats.removeConditionEffect(e);
    return _condition.remove(e);
  }

  public void applyConditionEffects() {
    _condition.applyEffects(this);
  }

  public String[] lastConditionMessage() {
    return _condition.lastMessage();
  }

  public boolean canAttack() {
    return _condition.canAttack();
  }

  public double catchBonus() {
    return _condition.getCatchBonus();
  }

  /**
   * Changes a Pokemon into another one. This can be regular evolution
   * (Charmander to Charmeleon) or other complicated changes (fire stone changes
   * Eevee into Flareon).
   */
  public void evolve(int... num) {
    _stats.points(_stats.points() + JPokemonConstants.STAT_POINTS_PER_EVOLUTION);

    if (num.length != 0)
      _number = num[0]; // special value
    else
      _number++;

    _moves.setPokemonNumber(_number);

    _species = PokemonInfo.get(_number);
    _stats.rebase(_species);

    checkNewMoves();
  }

  public JSONObject toJSON(TrainerState state) {
    JSONObject data = new JSONObject();

    try {

      if (state == TrainerState.BATTLE) {
        data.put("name", name());
        data.put("number", number());
        data.put("level", level());
        data.put("xp", xp());
        data.put("xp_needed", xpNeeded());
        data.put("hp", health());
        data.put("hp_max", maxHealth());
        data.put("condition", _condition.toString());
        data.put("moves", _moves.toJSON());
      }
      else if (state == TrainerState.UPGRADE) {
        data.put("name", name());
        data.put("number", number());
        data.put("level", level());
        data.put("xp", xp());
        data.put("points", _stats.points());
        data.put("stats", _stats.toJSON(state));
      }

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    if (_name != null) {
      node.setAttribute("name", _name);
    }

    node.setAttribute("number", _number + "");
    node.setAttribute("level", _level + "");
    node.setAttribute("xp", _xp + "");
    node.setAttribute("ot", _ot);
    node.setAttribute("has_original_trainer", _hasOriginalTrainer + "");

    node.addChild(_condition.toXML());
    node.addChild(_stats.toXML());
    node.addChild(_moves.toXML());

    return node;
  }

  public void loadXML(XMLNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XMLException("Cannot read node");

    if (node.getAttribute("name") != null)
      _name = node.getAttribute("name");

    _number = Integer.parseInt(node.getAttribute("number"));
    _moves.setPokemonNumber(_number);
    _species = PokemonInfo.get(_number);
    _stats.rebase(_species);

    _level = Integer.parseInt(node.getAttribute("level"));
    _stats.level(_level);

    _xp = Integer.parseInt(node.getAttribute("xp"));

    _ot = node.getAttribute("ot");

    _hasOriginalTrainer = Boolean.parseBoolean(node.getAttribute("has_original_trainer"));

    _moves.loadXML(node.getChildren(MoveBlock.XML_NODE_NAME).get(0));
    _stats.loadXML(node.getChildren(StatBlock.XML_NODE_NAME).get(0));

    _condition.reset();
    String conditionString = node.getChildren(Condition.XML_NODE_NAME).get(0).getValue();
    if (conditionString != null) {
      for (String ce : conditionString.replace('[', ' ').replace(']', ' ').trim().split(",")) {
        if (ce.isEmpty())
          continue;
        addConditionEffect(ConditionEffect.valueOf(ce));
      }
    }
  }

  private void checkNewMoves() {
    if (!_moves.newMoves(level()).isEmpty())
      ; // TODO : notify of new moves
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pokemon))
      return false;

    Pokemon p = (Pokemon) o;

    if (number() != p.number())
      return false;
    if (level() != p.level())
      return false;
    if (xp() != p.xp())
      return false;
    if (!name().equals(p.name()))
      return false;
    // Probably good enough...

    return true;
  }

  @Override
  public int hashCode() {
    return (name().hashCode() & 255) + _number + _level;
  }

  private MoveBlock _moves;
  private StatBlock _stats;
  private String _name, _ot;
  private Condition _condition;
  private PokemonInfo _species;
  private int _number, _level, _xp;
  private boolean _hasOriginalTrainer;
}