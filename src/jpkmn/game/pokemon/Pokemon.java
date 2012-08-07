package jpkmn.game.pokemon;

import java.util.Scanner;

import jpkmn.exceptions.CancelException;
import jpkmn.exceptions.LoadException;
import jpkmn.game.player.AbstractPlayer;
import jpkmn.game.pokemon.move.MoveBlock;
import jpkmn.game.pokemon.stat.StatBlock;
import jpkmn.game.base.PokemonBase;

public class Pokemon {
  public final Condition condition;
  public final StatBlock stats;
  public final MoveBlock moves;

  public Pokemon(int num, int lvl) {
    number = num;
    level = lvl;
    _xp = 0;
    condition = new Condition(this);
    stats = new StatBlock(this);
    moves = new MoveBlock(this);

    PokemonBase base = PokemonBase.getBaseForNumber(number);
    evolutionlevel = base.getEvolutionlevel();
    name = species = base.getName();
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());

    _id = CURRENT_ID++;
  }

  public int number() {
    return number;
  }

  public String name() {
    return name;
  }

  public void name(String s) {
    name = s;
  }

  public int level() {
    return level;
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

  public void xp(int amount) {
    _xp += amount;
    if (_xp >= getXPNeeded()) levelUp();
  }

  /**
   * XP this Pokemon needs to level
   * 
   * @return The amount of XP needed to gain a level
   */
  public int getXPNeeded() {
    return (int) (Math.log((double) level) * level * level * .35);
  }

  /**
   * Takes a specified amount of damage. If damage is greater than available
   * health, the Pokemon is knocked out.
   * 
   * @param damage The amount of damage to be taken
   * @return the awake state of the Pokemon
   */
  public void takeDamage(int damage) {
    stats.hp.effect(-damage);
    if (stats.hp.cur() == 0) condition.awake(false);
  }

  /**
   * Heals specified damage. If healed amount is greater than missing health,
   * Pokemon is brought to full health.
   * 
   * @param heal The amount healed by
   */
  public void healDamage(int heal) {
    condition.awake(true);
    stats.hp.effect(heal);
  }

  public void owner(AbstractPlayer owner) {
    _owner = owner;
  }

  public AbstractPlayer owner() {
    return _owner;
  }

  /**
   * Properly initializes a Pokemon from a file.
   * 
   * @param s String save representation of the pokemon.
   * @return A new Pokemon as described by the string
   * @throws LoadException if loaded with invalid string
   */
  public static Pokemon createFromString(String s) throws LoadException {
    if (s != null && !s.equals("||")) {
      try {
        Scanner scan = new Scanner(s);

        if (!(scan.next().equals("|("))) throw new Exception();

        Pokemon p = new Pokemon(scan.nextInt(), scan.nextInt());
        p.stats.setPoints(scan.nextInt());
        p._xp = scan.nextInt();

        if (!scan.next().equals(")")) throw new Exception();

        p.stats.atk.setPts(scan.nextInt());
        p.stats.stk.setPts(scan.nextInt());
        p.stats.def.setPts(scan.nextInt());
        p.stats.sdf.setPts(scan.nextInt());
        p.stats.spd.setPts(scan.nextInt());
        p.stats.resetMaxAll();

        if (!scan.next().equals("(")) throw new Exception();

        p.moves.removeAll();
        for (String next = scan.next(); !next.equals(")"); next = scan.next())
          p.moves.add(Integer.parseInt(next));

        p.name = scan.nextLine();
        p.name = p.name.substring(1, p.name.lastIndexOf("|") - 1);

        return p;

      } catch (Throwable t) {
        throw new LoadException("Pokemon loaded with string: " + s);
      }
    }

    return null;
  }

  /**
   * Properly writes this Pokemon to a save file
   */
  public String saveToString() {
    StringBuilder save = new StringBuilder();

    save.append("|( ");
    save.append(number + " " + level + " " + stats.getPoints() + " " + _xp
        + " ) ");
    save.append(stats.atk.pts() + " ");
    save.append(stats.stk.pts() + " ");
    save.append(stats.def.pts() + " ");
    save.append(stats.sdf.pts() + " ");
    save.append(stats.spd.pts() + " ");
    save.append("( ");
    try {
      for (int i = 0; i < moves.amount(); i++) {
        save.append(moves.get(i).number() + " ");
      }
    } catch (Exception e) {
      // do nothing
    }
    save.append(") " + name + " |");

    return save.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pokemon))
      return false;
    else
      return ((Pokemon) o)._id == this._id;
  }

  /**
   * Applies the level up. Level++, points++, stats adjusted, and checks for
   * evolution. Resets status conditions. Calls gui.Tools to notify the user
   * about the level up.
   */
  private void levelUp() {
    _xp -= getXPNeeded();
    level++;
    moves.check();
    stats.levelUp();
    condition.reset();
    if (level == evolutionlevel) changeSpecies();
  }

  /**
   * Calls gui.Tools to ask about evolution. If yes, increases number, adds 2
   * points, sets xp = 0, stats adjusted.
   */
  public boolean changeSpecies(int... num) {
    String speciesUpdate = "Your " + species + " evolved into ";

    try {
      if (!_owner.screen.isEvolutionOkay(this)) return false;
    } catch (CancelException c) {
      return false;
    }

    if (num == null || num.length == 0)
      number++;
    else
      number = num[0];

    PokemonBase base = PokemonBase.getBaseForNumber(number);
    evolutionlevel = base.getEvolutionlevel();
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());

    if (name.equals(species))
      name = species = base.getName();
    else
      species = base.getName();

    stats.changeSpecies(num);
    moves.check();

    speciesUpdate += species + "!";
    _owner.screen.notify("Congratulations!", speciesUpdate);

    return true;
  }

  public int hashCode() {
    return _id;
  }

  private int _id;
  private Type type1, type2;
  private String name, species;
  private AbstractPlayer _owner;
  private int number, level, _xp, evolutionlevel;

  private static int CURRENT_ID;
}