package pokemon;

import java.util.*;
import java.io.PrintWriter;

import gui.*;
import jpkmn.Driver;
import pokemon.move.*;
import lib.PokemonBase;

public class Pokemon {
  public final Condition condition;
  public final StatBlock stats;
  public Move[] move = new Move[4];

  private static long CURRENT_ID = 0;

  /**
   * Makes a new Pokemon of the specified number. All stats are stock.
   * 
   * @param num number of the pokemon
   * @param lvl level of the pokemon
   */
  public Pokemon(int num, int lvl) {
    number = num;
    level = lvl;
    xp = 0;
    condition = new Condition(this);
    stats = new StatBlock(this);

    PokemonBase base = PokemonBase.getBaseForNumber(number);
    evolutionlevel = base.getEvolutionlevel();
    name = species = base.getName();
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());

    setDefaultMoves();

    unique_id = CURRENT_ID++;
  }

  public Pokemon clone() {
    Pokemon p = new Pokemon(number, level);
    p.stats.hp.cur = this.stats.hp.cur;
    return p;
  }

  /**
   * Calls gui.Tools to ask about evolution. If yes, increases number, adds 2
   * points, sets xp = 0, stats adjusted.
   */
  public boolean changeSpecies(int... num) {
    if (!gui.Tools.askEvolution(this)) return false;

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
    checkNewMoves();

    return true;
  }

  public int number() {
    return number;
  }

  public String name() {
    return name;
  }

  public void nickname(String s) {
    name = s;
  }

  public String species() {
    return species;
  }

  public int level() {
    return level;
  }

  public boolean isAwake() {
    return condition.getAwake();
  }

  public Type type1() {
    return type1;
  }

  public Type type2() {
    return type2;
  }

  /**
   * Pokemon gains the experience amount. If it gains enough, it will level up.
   * 
   * @param amount The amount of xp gained
   */
  public void gainXP(int amount) {
    xp += amount;
    if (xp >= getXPNeeded()) levelUp();
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
   * XP this Pokemon gives if it is defeated
   * 
   * @return
   */
  public int getXPAwarded() {
    double factor = (Math.random() * .5 + 2);
    return (int) (factor * level);
  }

  /**
   * Takes a specified amount of damage. If damage is greater than available
   * health, the Pokemon is knocked out.
   * 
   * @param damage The amount of damage to be taken
   * @return the awake state of the Pokemon
   */
  public void takeDamage(int damage) {
    stats.hp.cur -= damage;
    if (stats.hp.cur <= 0) {
      stats.hp.cur = 0;
      condition.setAwake(false);
    }
  }

  /**
   * Heals specified damage. If healed amount is greater than missing health,
   * Pokemon is brought to full health.
   * 
   * @param heal The amount healed by
   */
  public void healDamage(int heal) {
    condition.setAwake(true);
    stats.hp.cur += heal;
    if (stats.hp.cur > stats.hp.max) {
      stats.hp.cur = stats.hp.max;
    }
  }

  public boolean canAttack() {
    return condition.canAttack();
  }

  /**
   * Used when the user attacks itself in confusion.
   */
  public void confusedAttack() {
    gui.Tools.notify(this, "CONFUSION", name + " hurt itself in confusion!");

    takeDamage(battle.Battle.confusedDamage(this));
  }

  private void checkNewMoves() {
    Move m = Move.getNewMove(this, level);
    if (m == null) return; // Nothing new

    // Don't allow duplicates
    for (Move x : move)
      if (m.equals(x)) return;

    // Ask for a position
    int pos = Tools.askMove(this, m);

    // User canceled learning new move
    if (pos == -1)
      return;

    // Set new move position
    else
      move[pos] = m;
  }

  /**
   * Picks up to 4 moves randomly from the list of moves that this Pokemon
   * could have learned by this level, and assigns them.
   */
  private void setDefaultMoves() {
    int move_num = 0;
    ArrayList<Move> moves = new ArrayList<Move>();

    for (int l = 1; l <= level; l++) {
      Move m = Move.getNewMove(this, l);
      if (m != null && !moves.contains(m)) moves.add(m);
    }
    Driver.log(Pokemon.class, name + " is selecting default moves from "
        + moves.toString());

    while (!moves.isEmpty() && move_num != 4) {
      int r = (int) (Math.random() * moves.size());
      move[move_num++] = moves.get(r);
      moves.remove(r);
    }

    Driver.log(Pokemon.class, name + " selected default moves: "
        + getMoveList());
  }

  public String getMoveList() {
    String response = "[";

    for (int i = 0; i < 4; ++i) {
      if (move[i] != null) response += move[i].name;
      if (i != 3) response += ", ";
    }

    return (response + "]");
  }

  /**
   * Properly initializes a Pokemon from a file.
   * 
   * @param s Scanner that points to the file. The '|' character should already
   *          be consumed
   * @return A new Pokemon as described by the file
   */
  public static Pokemon fromFile(Scanner s) {
    if (s == null || !s.hasNextLine()) return null;

    if (!(s.next().equals("("))) {
      Splash.showFatalErrorMessage("pokemon load fail");
    }

    Pokemon p = new Pokemon(s.nextInt(), s.nextInt());
    p.stats.setPoints(s.nextInt());
    p.xp = s.nextInt();

    if (!(s.next().equals(")")))
      Splash.showFatalErrorMessage("Insufficient basic data");

    try {
      p.stats.atk.pts = s.nextInt();
      p.stats.stk.pts = s.nextInt();
      p.stats.def.pts = s.nextInt();
      p.stats.sdf.pts = s.nextInt();
      p.stats.spd.pts = s.nextInt();
      p.stats.resetMaxAll();
    } catch (Exception e) {
      e.printStackTrace();
      Splash.showFatalErrorMessage("insufficient stat values");
    }

    if (!s.next().equals("("))
      Splash.showFatalErrorMessage("excessive stat values");

    int i = 0;
    for (String next = s.next(); !next.equals(")"); next = s.next(), ++i) {
      p.move[i] = new Move(Integer.parseInt(next), p);
    }

    p.name = s.nextLine();
    if (p.name.indexOf("|") == -1)
      Splash.showFatalErrorMessage("naming error");
    p.name = p.name.substring(1, p.name.indexOf("|") - 1);

    return p;
  }

  /**
   * Properly writes this Pokemon to a save file
   * 
   * @param p Instantiated PrintWriter that points to the file where the
   *          Pokemon should be written.
   */
  public void toFile(PrintWriter p) {
    p.print("| ( ");
    p.print(number + " " + level + " " + stats.getPoints() + " " + xp + " ) ");
    p.print(stats.atk.pts + " ");
    p.print(stats.stk.pts + " ");
    p.print(stats.def.pts + " ");
    p.print(stats.sdf.pts + " ");
    p.print(stats.spd.pts + " ");
    p.print("( ");
    try {
      for (int i = 0; i < 4; i++) {
        if (move[i] != null) p.print(move[i].number + " ");
      }
    } catch (Exception e) {
      // do nothing
    }
    p.println(") " + name + " |");
  }

  @Override
  public String toString() {
    return name + "(" + unique_id + ") LVL. " + level + " HP: " + stats.hp.cur
        + "/" + stats.hp.max;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pokemon))
      return false;
    else
      return ((Pokemon) o).unique_id == this.unique_id;
  }

  /**
   * Applies the level up. Level++, points++, stats adjusted, and checks for
   * evolution. Resets status conditions. Calls gui.Tools to notify the user
   * about the level up.
   */
  private void levelUp() {
    xp -= getXPNeeded();
    level++;
    stats.levelUp();
    condition.reset();
    gui.Tools.notify(this, "LEVEL UP", name + " reached level " + level + "!");
    checkNewMoves();
    if (level == evolutionlevel) changeSpecies();
  }

  private int number, level, xp, evolutionlevel;
  private String name, species;
  private Type type1, type2;
  private long unique_id;
}