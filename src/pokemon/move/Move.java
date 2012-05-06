package pokemon.move;

import java.util.ArrayList;
import java.util.List;

import lib.BonusEffectBase;
import lib.MoveBase;

import gui.Tools;
import jpkmn.Driver;
import pokemon.Pokemon;
import pokemon.Type;
import battle.Target;

public class Move {
  public final Pokemon pkmn;

  /**
   * Creates a new move of the specified number
   * 
   * @param num The number of the move
   * @param user The user of the move
   */
  public Move(int num, Pokemon user) {
    number = num;
    pkmn = user;
    enabled = true;

    MoveBase base = MoveBase.getBaseForNumber(number);
    name = base.getName();
    power = base.getPower();
    pp = ppmax = base.getPp();
    accuracy = base.getAccuracy();
    type = Type.valueOf(base.getType());
    style = MoveStyle.valueOf(base.getStyle());

    setBonusEffects();
  }

  public String name() {
    return name;
  }

  public int number() {
    return number;
  }

  public int power() {
    return power;
  }

  public MoveStyle style() {
    return style;
  }

  /**
   * Tells whether it is valid to use this move. This method will reduce PP.
   * Note that it is not appropriate to call this method on repeat-style moves,
   * or multi-turn moves.
   * 
   * @return True if the move can be performed this turn
   */
  public boolean use() {
    return enabled = enabled && pp-- >= 0;
  }

  /**
   * Reset base attributes for a move
   */
  public void restore() {
    pp = ppmax;
    enabled = true;
  }

  /**
   * Determine whether a move is normal, super, or not very effective
   * 
   * @param p The pokemon targeted by the move
   * @return How effect the move is
   */
  public double effectiveness(Pokemon p) {
    return type.effectiveness(p);
  }

  /**
   * Tells the Same-Type-Attack-Bonus advantage. 1.5 is true, 1 if false
   * 
   * @return the STAB advantage
   */
  public double STAB() {
    return (type == pkmn.type1() || type == pkmn.type2()) ? 1.5 : 1.0;
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
      if (p.level() > pkmn.level()) {
        Tools
            .notify(pkmn, "FAIL", "OHKO moves can't be used on higher levels!");
        Driver.log(Move.class, "OHKO Move used on higher level. Move = " + name
            + ". User/Target = " + pkmn.name() + "/" + p.name());
        return false;
      }
      else {
        if ((pkmn.level() - p.level() + 30.0) / 100.0 > Math.random()) {
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

  @Override
  public String toString() {
    return name + " (" + pp + "/" + ppmax + ")";
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Move)) return false;

    return number == ((Move) o).number;
  }

  /**
   * Loads and sets all the bonus effects for a move
   */
  private void setBonusEffects() {
    BonusEffect current;
    be = new ArrayList<BonusEffect>();

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

  private String name;
  private int number, power, pp, ppmax;
  private double accuracy;
  private boolean enabled = true;
  private Type type;
  private MoveStyle style;
  private List<BonusEffect> be;
}