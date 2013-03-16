package org.jpokemon.pokemon;

import java.util.List;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveBlock;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.stat.StatBlock;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.trainer.TrainerState;
import org.json.JSONException;
import org.json.JSONObject;

public class Pokemon implements JPokemonConstants {
  public Pokemon(int num) {
    _number = num;
    _caughtLocation = -1;
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
    if (name == null)
      return species();

    return name;
  }

  public void name(String s) {
    name = s;
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
      _stats.points(_stats.points() + STAT_POINTS_PER_LEVEL);

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

  public int caughtLocation() {
    return _caughtLocation;
  }

  public String originalTrainer() {
    return _originalTrainer;
  }

  public void caughtByAt(String name, int location) {
    if (_caughtLocation != -1)
      throw new IllegalStateException("Origin already accounted for");

    _caughtLocation = location;
    _originalTrainer = name;
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
    _stats.points(_stats.points() + STAT_POINTS_PER_EVOLUTION);

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

  /**
   * Properly initializes a Pokemon from a file.
   * 
   * @param s String save representation of the pokemon.
   * @return A new Pokemon as described by the string
   * @throws LoadException if loaded with invalid string
   */
  public static Pokemon load(String s) throws LoadException {
    if (s != null) {

      Scanner scan = new Scanner(s);

      scan.next(); // Throw away "|( "

      Pokemon p = new Pokemon(scan.nextInt());
      p.level(scan.nextInt());
      p._stats.points(scan.nextInt());
      p._xp = scan.nextInt();

      if (!scan.next().equals(")")) {
        scan.close();
        throw new LoadException("Formatting error");
      }

      p.getStat(StatType.ATTACK).points(scan.nextInt());
      p.getStat(StatType.SPECATTACK).points(scan.nextInt());
      p.getStat(StatType.DEFENSE).points(scan.nextInt());
      p.getStat(StatType.SPECDEFENSE).points(scan.nextInt());
      p.getStat(StatType.SPEED).points(scan.nextInt());
      p._stats.reset();

      if (!scan.next().equals("(")) {
        scan.close();
        throw new LoadException("Formatting error");
      }

      p._moves.removeAll();
      for (String next = scan.next(); !next.equals(")"); next = scan.next())
        p._moves.add(Integer.parseInt(next));

      p.name = scan.nextLine();
      p.name = p.name.substring(1, p.name.lastIndexOf("|") - 1);

      scan.close();
      return p;
    }

    return null;
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
  private Condition _condition;
  private PokemonInfo _species;
  private String name, _originalTrainer;
  private int _number, _level, _xp, _caughtLocation;
}