package jpkmn.game.pokemon;

import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.base.PokemonBase;
import jpkmn.game.player.Trainer;

import org.jpokemon.pokemon.Type;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveBlock;
import org.jpokemon.pokemon.stat.Health;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.stat.StatBlock;
import org.jpokemon.pokemon.stat.StatType;

public class Pokemon {
  public final MoveBlock moves;
  public final Condition condition;

  public Pokemon(int num) throws LoadException {
    _number = num;

    PokemonBase base = PokemonBase.get(_number);

    name = species = base.getName();
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());
    evolutionlevel = base.getEvolutionlevel();

    moves = new MoveBlock(_number);
    _stats = new StatBlock(base);
    condition = new Condition(this);

    _id = CURRENT_ID++;
  }

  public Pokemon(int num, int lvl) throws LoadException {
    this(num);
    _level = lvl;
    _stats.level(lvl);
    moves.randomize(lvl);
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

    if (_xp >= xpNeeded()) {
      _xp -= xpNeeded();
      _stats.points(_stats.points() + 1);
    }

    checkNewMoves();

    condition.reset();
  }

  public Trainer owner() {
    return _owner;
  }

  public void owner(Trainer owner) {
    _owner = owner;
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

    if (_xp >= xpNeeded())
      level(level() + 1);
  }

  public Stat getStat(StatType s) {
    return _stats.get(s);
  }

  public int health() {
    return _stats.get(StatType.HEALTH).cur();
  }

  public int maxHealth() {
    return ((Health) getStat(StatType.HEALTH)).max();
  }

  public int attack() {
    return _stats.get(StatType.ATTACK).cur();
  }

  public int specattack() {
    return _stats.get(StatType.SPECATTACK).cur();
  }

  public int defense() {
    return _stats.get(StatType.DEFENSE).cur();
  }

  public int specdefense() {
    return _stats.get(StatType.SPECDEFENSE).cur();
  }

  public int speed() {
    return _stats.get(StatType.SPEED).cur();
  }

  /**
   * XP this Pokemon needs to level
   * 
   * @return The amount of XP needed to gain a level
   */
  public int xpNeeded() {
    return (int) (Math.log((double) _level) * _level * .35 * _level);
  }

  /**
   * Changes a Pokemon into another one. This can be regular evolution
   * (Charmander to Charmeleon) or other complicated changes (fire stone
   * changes Eevee into Flareon).
   * 
   * Evolve will fail if the Pokemon is not high enough level, and no arguments
   * are passed
   */
  public void evolve(int... num) {
    String speciesUpdate = "Your " + species + " evolved into ";

    // No points for Vaporeon/Jolteon/Flareon
    if (_number < 134 || _number > 136)
      _stats.points(_stats.points() + 1);

    if (num.length != 0)
      _number = num[0]; // special value
    else if (_level < evolutionlevel)
      return; // stock evolution and they cannot evolve yet.
    else
      _number++;

    PokemonBase base = PokemonBase.get(_number);

    moves.setPokemonNumber(_number);
    checkNewMoves();

    _stats.rebase(base);
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());
    evolutionlevel = base.getEvolutionlevel();

    if (name.equals(species))
      name = species = base.getName();
    else
      species = base.getName();

    speciesUpdate += species + "!";
    _owner.screen.notify("Congratulations!", speciesUpdate);
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

    if (health() == 0)
      condition.awake(false);
  }

  /**
   * Heals specified damage. If healed amount is greater than missing health,
   * Pokemon is brought to full health.
   * 
   * @param heal The amount healed by
   */
  public void healDamage(int heal) {
    condition.awake(true);
    getStat(StatType.HEALTH).effect(heal);
  }

  public void addIssue(Condition.Issue i) {
    _stats.addIssue(i);
    condition.add(i);
  }

  public boolean hasIssue(Condition.Issue i) {
    return condition.contains(i);
  }

  public void removeIssue(Condition.Issue i) {
    _stats.removeIssue(i);
    condition.remove(i);
  }

  /**
   * Properly writes this Pokemon to a save file
   */
  public String save() {
    StringBuilder save = new StringBuilder();

    save.append("|( ");
    save.append(_number);
    save.append(" ");
    save.append(_level);
    save.append(" ");
    save.append(_stats.points());
    save.append(" ");
    save.append(_xp);
    save.append(" ) ");
    save.append(getStat(StatType.ATTACK).points());
    save.append(" ");
    save.append(getStat(StatType.SPECATTACK).points());
    save.append(" ");
    save.append(getStat(StatType.DEFENSE).points());
    save.append(" ");
    save.append(getStat(StatType.SPECDEFENSE).points());
    save.append(" ");
    save.append(getStat(StatType.SPEED).points());
    save.append(" ( ");

    for (Move move : moves)
      save.append(move.number() + " ");

    save.append(") " + name + " |\n");

    return save.toString();
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

      p.moves.removeAll();
      for (String next = scan.next(); !next.equals(")"); next = scan.next())
        p.moves.add(Integer.parseInt(next));

      p.name = scan.nextLine();
      p.name = p.name.substring(1, p.name.lastIndexOf("|") - 1);

      return p;
    }

    return null;
  }

  private void checkNewMoves() {
    if (!moves.newMoves(level()).isEmpty())
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

  private Trainer _owner;
  private StatBlock _stats;
  private Type type1, type2;
  private String name, species;
  private int _id, _number, _level, _xp, evolutionlevel;

  private static int CURRENT_ID;
}