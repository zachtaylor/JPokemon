package pokemon.move;

import gui.Tools;

import java.util.ArrayList;

import jpkmn.Driver;
import lib.BonusEffectBase;
import lib.MoveBase;
import lib.MoveMap;
import pokemon.*;
import battle.Target;

public class Move {
  public Type type;
  public Pokemon pkmn;
  public int number, power, pp, ppmax;
  public double accuracy;
  public String name;
  public MoveStyle style;
  public ArrayList<BonusEffect> be = new ArrayList<BonusEffect>();
  public boolean enabled = true;
  private int a;

  /**
   * Creates a new move of the specified number
   * 
   * @param num The number of the move
   * @param user The user of the move
   */
  public Move(int num, Pokemon user) {
    number = num;
    pkmn = user;

    setBonusEffects();

    resetBase();
  }

  /**
   * Tells the STAB advantage. 1.5 is true, 1 if false
   *
   * @return the STAB advantage
   */
  public double STAB() {
    return (type == pkmn.type1 || type == pkmn.type2) ? 1.5 : 1.0;
  }

  /**
   * Determine whether a move is
   * normal, super, or not very effective
   *
   * @param p The pokemon targeted by the move
   * @return How effect the move is
   */
  public double effectiveness(Pokemon p) {
    return type.effectiveness(p);
  }

  /**
   * Reset base attributes for a move
   */
  public void resetBase() {
    MoveBase base = MoveBase.getBaseForNumber(number);

    accuracy = base.getAccuracy();
    name = base.getName();
    power = base.getPower();
    ppmax = base.getPp();
    pp = ppmax;
    style = MoveStyle.valueOf(base.getStyle());
    type = Type.valueOf(base.getType());
  }

  /**
   * Figures out whether this move hits the target. Each time hits is called,
   * it is random. Therefore, do not call it multiple times per attempted
   * attack. This method only computes the probability of hitting, but does not
   * effect pp.
   *
   * @param p Target pokemon
   * @return True if the move hits
   */
  public boolean hits(Pokemon p) {

    // Move # 141 (Swift) never misses
    if (number == 141) {
      Driver.log(Move.class, "Swift auto-succeeds.");
      return true;
    }

    if (style == MoveStyle.OHKO) {
      if (p.level > pkmn.level) {
        Tools
            .notify(pkmn, "FAIL", "OHKO moves can't be used on higher levels!");
        Driver.log(Move.class, "OHKO Move used on higher level. Move = " + name
            + ". User/Target = " + pkmn.name + "/" + p.name);
        return false;
      }
      else {
        if ((pkmn.level - p.level + 30.0) / 100.0 > Math.random()) {
          Driver.log(Move.class, "OHKO move successful");
          return true;
        }
        else {
          Tools.notify(pkmn, "MISS", name + " missed!");
          Driver.log(Move.class, "OHKO move missed.");
          return false;
        }
      }
    }
    else {
      return Math.random() <= accuracy;
    }
  }

  public static Move getNewMove(Pokemon p, int level) {
    MoveMap m = MoveMap.getMapForPokemonNumberAtLevel(p.number, level);

    // Return null if there isn't a move for this level, or construct the move
    return m == null ? null : new Move(m.getMove_number(), p);
  }

  /**
   * Loads and sets all the bonus effects for a move
   */
  private void setBonusEffects() {
      BonusEffect current;
      for (BonusEffectBase base : BonusEffectBase.getBasesForMoveNumber(number)) {
          // Get Bonus Effect type
          current = BonusEffect.valueOf(base.getType());
          // Add all attributes, including unused
          current.target = Target.valueOf(base.getTarget());
          current.chance = base.getChance();
          current.percent = base.getPercent();
          current.power = base.getPower();
          // Add to moves list of effects
          be.add(current);
      }
  }

  @Override
  public String toString() {
    return name + " (" + pp + "/" + ppmax + ")";
  }

  @Override
  public boolean equals(Object m) {
    if (!(m instanceof Move))
      return false;
    Move n = (Move) m; // MUpdated. makes life easier
    return (number == n.number && n.name.equals(name));
  }
}