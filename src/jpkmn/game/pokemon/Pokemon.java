package jpkmn.game.pokemon;

import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.base.PokemonBase;
import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.move.MoveBlock;

import org.jpokemon.pokemon.stat.Health;
import org.jpokemon.pokemon.stat.Stat;
import org.jpokemon.pokemon.stat.StatBlock;
import org.jpokemon.pokemon.stat.StatType;

public class Pokemon {
  public final MoveBlock moves;
  public final Condition condition;

  public Pokemon(int num) {
    _number = num;

    PokemonBase base = PokemonBase.get(_number);

    name = species = base.getName();
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());
    evolutionlevel = base.getEvolutionlevel();

    moves = new MoveBlock(this);
    _stats = new StatBlock(base);
    condition = new Condition(this);

    _id = CURRENT_ID++;
  }

  public Pokemon(int num, int lvl) {
    this(num);
    _level = lvl;
    _stats.level(lvl);
    moves.randomize();
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

    moves.check();
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
  
  public Health health() {
    return (Health) _stats.get(StatType.HEALTH);
  }

  public Stat attack() {
    return _stats.get(StatType.ATTACK);
  }

  public Stat specattack() {
    return _stats.get(StatType.SPECATTACK);
  }

  public Stat defense() {
    return _stats.get(StatType.DEFENSE);
  }

  public Stat specdefense() {
    return _stats.get(StatType.SPECDEFENSE);
  }

  public Stat speed() {
    return _stats.get(StatType.SPEED);
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

    moves.check();
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
    health().effect(-damage);

    if (health().cur() == 0)
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
    health().effect(heal);
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
    save.append(attack().points());
    save.append(" ");
    save.append(specattack().points());
    save.append(" ");
    save.append(defense().points());
    save.append(" ");
    save.append(specdefense().points());
    save.append(" ");
    save.append(speed().points());
    save.append(" ( ");

    try {
      for (int i = 0; i < moves.amount(); i++) {
        save.append(moves.get(i).number() + " ");
      }
    } catch (Exception e) {
      // do nothing
    }
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
      try {
        Scanner scan = new Scanner(s);

        if (!(scan.next().equals("|(")))
          throw new Exception();

        Pokemon p = new Pokemon(scan.nextInt());
        p.level(scan.nextInt());
        p._stats.points(scan.nextInt());
        p._xp = scan.nextInt();

        if (!scan.next().equals(")"))
          throw new Exception();

        p.attack().points(scan.nextInt());
        p.specattack().points(scan.nextInt());
        p.defense().points(scan.nextInt());
        p.specdefense().points(scan.nextInt());
        p.speed().points(scan.nextInt());
        p._stats.reset();

        if (!scan.next().equals("("))
          throw new Exception();

        p.moves.removeAll();
        for (String next = scan.next(); !next.equals(")"); next = scan.next())
          p.moves.add(Integer.parseInt(next));

        p.name = scan.nextLine();
        p.name = p.name.substring(1, p.name.lastIndexOf("|") - 1);

        return p;

      } catch (Throwable t) {
        throw new LoadException("Pokemon could not load: " + s);
      }
    }

    return null;
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