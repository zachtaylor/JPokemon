package pokemon;

import java.util.*;
import java.io.PrintWriter;

import gui.*;
import jpkmn.Driver;
import pokemon.stat.*;
import pokemon.move.*;
import lib.PokemonBase;

public class Pokemon {
  public Type type1, type2;
  public int number, level, points, xp;
  public Stat attack, specattack, defense, specdefense, speed, health;
  public String name;
  public boolean awake = true;
  public Status status;
  public Move[] move = new Move[4];
  private int evolutionlevel;
  private int a;
  private int unique_id;
  
  private static int CURRENT_ID = 0;

  /**
   * Makes a new Pokemon of the specified number. All stats are stock.
   * 
   * @param num number of the pokemon
   * @param lvl level of the pokemon
   */
  public Pokemon(int num, int lvl) {
    number = num;
    level = lvl;
    points = 0;
    xp = 0;
    status = new Status(this);
    
    unique_id = CURRENT_ID++;
    PokemonBase base = PokemonBase.getBaseForNumber(number);
    name = base.getName();
    resetBase(base);
    
    setDefaultMoves();
  }

  /**
   * Resets temporary stats to their maximum values. Does NOT reset health.
   */
  public void resetTempStats() {
    attack.reset();
    specattack.reset();
    defense.reset();
    specdefense.reset();
    speed.reset();

    for (Move cur : move)
      if (cur != null)
        cur.resetBase();
  }

  /**
   * Takes a specified amount of damage. If damage is greater than available
   * health, the Pokemon is knocked out.
   * 
   * @param damage The amount of damage to be taken
   * @return the awake state of the Pokemon
   */
  public void takeDamage(int damage) {
    health.cur -= damage;
    if (health.cur <= 0) {
      health.cur = 0;
      awake = false;
    }
  }

  /**
   * Heals specified damage. If healed amount is greater than missing health,
   * Pokemon is brought to full health.
   * 
   * @param heal The amount healed by
   */
  public void healDamage(int heal) {
    health.cur += heal;
    if (health.cur > health.max) {
      health.cur = health.max;
    }
  }

  /**
   * Used when the user attacks itself in confusion.
   */
  public void confusedAttack() {
    gui.Tools.notify(this, "CONFUSION", name + " hurt itself in confusion!");

    takeDamage(battle.Battle.confusedDamage(this));
  }

  /**
   * Pokemon gains the experience amount. If it gains enough, it will
   * levelUp().
   * 
   * @param amount The amount of xp gained
   */
  public void gainExperience(int amount) {
    xp += amount;
    if (xp >= xpNeeded())
      levelUp();
  }

  /**
   * XP this Pokemon needs to level
   * 
   * @return The amount of XP needed to gain a level
   */
  public int xpNeeded() {
    double l = level;
    double factor = 4 / 5;
    return (int) (factor * l * l * l);
  }

  /**
   * XP this Pokemon gives if it is defeated
   * 
   * @return
   */
  public int xpGiven() {
    double xpwon = level;
    xpwon *= xpwon;
    xpwon *= xpwon;
    xpwon *= Math.random() * 0.65 + .3;
    xpwon /= 7;
    return (int) xpwon;
  }

  /**
   * Applies the level up. Level++, points++, stats adjusted, and checks for
   * evolution. Resets status conditions. Calls gui.Tools to notify the user
   * about the level up.
   */
  private void levelUp() {
    xp -= xpNeeded();
    level++;
    points++;
    attack.incLevel();
    specattack.incLevel();
    defense.incLevel();
    specdefense.incLevel();
    speed.incLevel();
    health.incLevel();
    gui.Tools.notify(this, "LEVEL UP", name + " reached level " + level + "!");
    status.reset();
    checkNewMoves();
    if (level == evolutionlevel)
      changeSpecies();
  }

  /**
   * Calls gui.Tools to ask about evolution. If yes, increases number, adds 2
   * points, sets xp = 0, stats adjusted.
   */
  public boolean changeSpecies(int... num) {
    if (!gui.Tools.askEvolution(this))
      return false;

    PokemonBase oldBase = PokemonBase.getBaseForNumber(number);

    if (num == null || num.length == 0) {
      number++;
      points++;
    }
    else {
      // Eevee gets points
      if (number == 133)
        points += 2;
      number = num[0];
    }

    // Proper handling for renaming
    PokemonBase newBase = PokemonBase.getBaseForNumber(number);
    name = name.equals(oldBase.getName()) ? newBase.getName() : name;

    resetBase(newBase);

    checkNewMoves();

    return true;
  }

  private void resetBase(PokemonBase base) {
    attack = new AtkStat(level, base.getAttack());
    specattack = new SpecAtkStat(level, base.getSpecattack());
    defense = new DefStat(level, base.getDefense());
    specdefense = new SpecDefStat(level, base.getSpecdefense());
    speed = new SpeedStat(level, base.getSpeed());
    health = new HPStat(level, base.getHealth());
    evolutionlevel = base.getEvolutionlevel();
    type1 = Type.valueOf(base.getType1());
    type2 = Type.valueOf(base.getType2());
    xp = 0;
  }

  private void checkNewMoves() {
    Move m = Move.getNewMove(this, level);
    if (m == null)
      return; // Nothing new

    // Don't allow duplicates
    for (Move x : move)
      if (m.equals(x))
        return;

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

    for (int l = level; l > 0; --l) {
      Move m = Move.getNewMove(this, l);
      if (m != null && !moves.contains(m))
        moves.add(m);
    }
    Driver.log(Pokemon.class, name + " is selecting default moves from "
        + moves.toString());

    while (!moves.isEmpty() && move_num != 4) {
      int r = (int) (Math.random() * moves.size());
      move[move_num++] = moves.get(r);
      moves.remove(r);
    }
    Driver.log(Pokemon.class,
        name + " selected default moves: " + getMoveList());
  }

  public String getMoveList() {
    String response = "[";

    for (int i = 0; i < 4; ++i) {
      if (move[i] != null)
        response += move[i].name;
      if (i != 3) response += ", ";
    }

    return (response + "]");
  }
  
  public boolean canAttack() {
    return status.canAttack();
  }

  public int numMoves() {
    int x = 0;
    for (int i = 0; i < 4; i++) {
      if (move[i] != null)
        ++x;
    }
    return x;
  }

  public Pokemon clone() {
    Pokemon p = new Pokemon(number, level);
    p.health.cur = this.health.cur;
    return p;
  }

  /**
   * Properly initializes a Pokemon from a file.
   * 
   * @param s Scanner that points to the file. The '|' character should already
   *          be consumed
   * @return A new Pokemon as described by the file
   */
  public static Pokemon fromFile(Scanner s) {
    if (s == null || !s.hasNextLine())
      return null;

    if (!(s.next().equals("("))) {
      Splash.showFatalErrorMessage("pokemon load fail");
    }
    
    Pokemon p = new Pokemon(s.nextInt(), s.nextInt());
    p.points = s.nextInt();
    p.xp = s.nextInt();

    if (!(s.next().equals(")")))
      Splash.showFatalErrorMessage("Insufficient basic data");

    try {
      p.attack.pts = s.nextInt();
      p.attack.resetMax();
      p.specattack.pts = s.nextInt();
      p.specattack.resetMax();
      p.defense.pts = s.nextInt();
      p.defense.resetMax();
      p.specdefense.pts = s.nextInt();
      p.specdefense.resetMax();
      p.speed.pts = s.nextInt();
      p.speed.resetMax();
      p.health.pts = s.nextInt();
      p.health.resetMax();
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
   * @param p Instantaited PrintWriter that points to the file where the
   *          Pokemon should be written.
   */
  public void toFile(PrintWriter p) {
    p.print("| ( ");
    p.print(number + " " + level + " " + points + " " + xp + " ) ");
    p.print(attack.pts + " ");
    p.print(specattack.pts + " ");
    p.print(defense.pts + " ");
    p.print(specdefense.pts + " ");
    p.print(speed.pts + " ");
    p.print(health.pts + " ");
    p.print("( ");
    try {
      for (int i = 0; i < 4; i++) {
        if (move[i] != null)
          p.print(move[i].number + " ");
      }
    } catch (Exception e) {
      // do nothing
    }
    p.println(") " + name + " |");
  }

  @Override
  public String toString() {
    return name + "("+unique_id+") LVL. " + level + " HP: " + health.cur + "/"
        + health.max;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Pokemon)) return false;
    else return ((Pokemon) o).unique_id == this.unique_id;
  }
}