package jpkmn.game.pokemon;

import java.util.Scanner;

import jpkmn.exceptions.LoadException;

import org.jpokemon.pokemon.Type;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveBlock;
import org.jpokemon.pokemon.stat.Health;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.stat.StatBlock;
import org.jpokemon.pokemon.stat.StatType;
import org.json.JSONException;
import org.json.JSONObject;

public class Pokemon {
  public Pokemon(int num) {
    _number = num;

    PokemonInfo base = PokemonInfo.get(_number);

    name = species = base.getName();
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());
    evolutionlevel = base.getEvolutionlevel();

    _moves = new MoveBlock(_number);
    _stats = new StatBlock(base);
    _condition = new Condition(this);

    _id = CURRENT_ID++;
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
    return name;
  }

  public void name(String s) {
    name = s;
  }

  public int level() {
    return _level;
  }

  public void level(int l) {
    _level = l;
    _stats.level(l);

    checkNewMoves();

    _condition.reset();
  }

  public Type type1() {
    return type1;
  }

  public Type type2() {
    return type2;
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
    _xp += amount;

    if (_xp >= xpNeeded()) {
      _xp -= xpNeeded();
      _stats.points(_stats.points() + 1);
      level(level() + 1);
    }
  }

  /**
   * XP this Pokemon needs to level
   * 
   * @return The amount of XP needed to gain a level
   */
  public int xpNeeded() {
    return (int) (Math.log((double) _level) * _level * .35 * _level);
  }

  public Stat getStat(StatType s) {
    return _stats.get(s);
  }

  public int health() {
    int val = getStat(StatType.HEALTH).cur();
    return val;
  }

  public int maxHealth() {
    return ((Health) getStat(StatType.HEALTH)).max();
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
    _condition.applyEffects();
  }

  public String[] lastConditionMessage() {
    return _condition.lastMessage();
  }

  public boolean canAttack() {
    return _condition.canAttack();
  }

  public int catchBonus() {
    return _condition.getCatchBonus();
  }

  /**
   * Changes a Pokemon into another one. This can be regular evolution
   * (Charmander to Charmeleon) or other complicated changes (fire stone changes
   * Eevee into Flareon).
   * 
   * Evolve will fail if the Pokemon is not high enough level, and no arguments
   * are passed
   */
  public void evolve(int... num) {
    // No points for Vaporeon/Jolteon/Flareon
    if (_number < 134 || _number > 136)
      _stats.points(_stats.points() + 1);

    if (num.length != 0)
      _number = num[0]; // special value
    else if (_level < evolutionlevel)
      throw new IllegalStateException(name() + " is not ready to evolve");
    else
      _number++;

    PokemonInfo info = PokemonInfo.get(_number);

    _moves.setPokemonNumber(_number);
    _stats.rebase(info);
    type1 = Type.valueOf(info.getType1());
    type2 = Type.valueOf(info.getType2());
    evolutionlevel = info.getEvolutionlevel();

    if (name.equals(species))
      name = species = info.getName();
    else
      species = info.getName();

    checkNewMoves();
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("name", name);
      data.put("number", _number);
      data.put("level", _level);
      data.put("xp", _xp);
      data.put("xp_needed", xpNeeded());
      data.put("condition", _condition.toString());
      data.put("stats", _stats.toJSON());
      data.put("moves", _moves.toJSON());

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
    if (s != null && !s.equals(" ")) {

      Scanner scan = new Scanner(s);

      if (!(scan.next().equals("|(")))
        throw new LoadException("Formatting error");

      Pokemon p = new Pokemon(scan.nextInt());
      p.level(scan.nextInt());
      p._stats.points(scan.nextInt());
      p._xp = scan.nextInt();

      if (!scan.next().equals(")"))
        throw new LoadException("Formatting error");

      p.getStat(StatType.ATTACK).points(scan.nextInt());
      p.getStat(StatType.SPECATTACK).points(scan.nextInt());
      p.getStat(StatType.DEFENSE).points(scan.nextInt());
      p.getStat(StatType.SPECDEFENSE).points(scan.nextInt());
      p.getStat(StatType.SPEED).points(scan.nextInt());
      p._stats.reset();

      if (!scan.next().equals("("))
        throw new LoadException("Formatting error");

      p._moves.removeAll();
      for (String next = scan.next(); !next.equals(")"); next = scan.next())
        p._moves.add(Integer.parseInt(next));

      p.name = scan.nextLine();
      p.name = p.name.substring(1, p.name.lastIndexOf("|") - 1);

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
    else
      return ((Pokemon) o)._id == this._id;
  }

  public int hashCode() {
    return _id;
  }

  private MoveBlock _moves;
  private StatBlock _stats;
  private Type type1, type2;
  private Condition _condition;
  private String name, species;
  private int _id, _number, _level, _xp, evolutionlevel;

  private static int CURRENT_ID;
}