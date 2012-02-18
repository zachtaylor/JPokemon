package pokemon.move;

import gui.Tools;

import java.util.ArrayList;

import jpkmn.Driver;
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
    Library.setBonusEffects(this);

    resetBase();
  }

  /**
   * Tells the STAB advantage. 1.5 is true, 1 if false
   * 
   * @return the STAB advantage
   */
  public double STAB() {
    if (type == pkmn.type1 || type == pkmn.type2)
      return 1.5;
    else
      return 1;
  }

  public double effectiveness(Pokemon p) {
    return type.effectiveness(p);
  }

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

  @Override
  public boolean equals(Object m) {
    if (!(m instanceof Move))
      return false;
    Move n = (Move) m; // MUpdated. makes life easier
    return (number == n.number && n.name.equals(name));
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

  @Override
  public String toString() {
    return name + " (" + pp + "/" + ppmax + ")";
  }

  public static Move getNewMove(Pokemon p, int level) {
    MoveMap m = MoveMap.getMapForPokemonNumberAtLevel(p.number, level);

    // Return null if there isn't a move for this level, or construct the move
    return m == null ? null : new Move(m.getMove_number(), p);
  }

  public static class Library {

    public static void setBonusEffects(Move m) {
      int num = m.number;
      ArrayList<BonusEffect> bonuseffects = m.be;
      bonuseffects.clear();

      BonusEffect current;
      switch (num) {
      case 1: // Absorb
        current = BonusEffect.HEAL;
        current.target = Target.SELF;
        current.chance = 1.0;
        current.percent = .25;
        bonuseffects.add(current);
        current = null;
        break;
      case 2: // Acid
        current = BonusEffect.SPECDEFENSE;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = .05;
        bonuseffects.add(current);
        current = null;
        current = BonusEffect.DEFENSE;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = .05;
        bonuseffects.add(current);
        current = null;
        break;
      case 3: // Acid Armor
        current = BonusEffect.DEFENSE;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 4: // Agility
        current = BonusEffect.SPEED;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 5: // Amnesia
        current = BonusEffect.SPECDEFENSE;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 6: // Aurora Beam
        current = BonusEffect.ATTACK;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 8: // Barrier
        current = BonusEffect.DEFENSE;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 10: // Bind
        current = BonusEffect.WRAP;
        current.target = Target.ENEMY;
        current.chance = .75;
        bonuseffects.add(current);
        current = null;
        break;
      case 11: // Bite
        current = BonusEffect.FLINCH;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 12: // Blizzard
        current = BonusEffect.FREEZE;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 13: // Body Slam
        current = BonusEffect.PARALYZE;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 14: // Bone Club
        current = BonusEffect.FLINCH;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 16: // Bubble
        current = BonusEffect.SPEED;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 17: // Bubblebeam
        current = BonusEffect.SPEED;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 18: // Clamp
        current = BonusEffect.WRAP;
        current.target = Target.ENEMY;
        current.chance = .75;
        bonuseffects.add(current);
        current = null;
        break;
      case 20: // Confuse Ray
        current = BonusEffect.CONFUSE;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 21: // Confusion
        current = BonusEffect.CONFUSE;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 22: // Constrict
        current = BonusEffect.SPEED;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 27: // Defense Curl
        current = BonusEffect.DEFENSE;
        current.target = Target.SELF;
        current.power = 1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 30: // Dizzy Punch
        current = BonusEffect.CONFUSE;
        current.target = Target.SELF;
        current.chance = .2;
        bonuseffects.add(current);
        current = null;
        break;
      case 32: // Double Team FLAG - EVASION
        current = BonusEffect.SPEED;
        current.target = Target.SELF;
        current.power = 1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 33: // Double-Edge
        current = BonusEffect.KAMIKAZE;
        current.target = Target.SELF;
        current.percent = .125;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 36: // Dream Eater
        current = BonusEffect.HEAL;
        current.target = Target.SELF;
        current.percent = .4;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 40: // Ember
        current = BonusEffect.BURN;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 42: // Fire Blast
        current = BonusEffect.BURN;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 43: // Fire Punch
        current = BonusEffect.BURN;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 44: // Fire Spin
        current = BonusEffect.WRAP;
        current.target = Target.ENEMY;
        current.chance = .7;
        bonuseffects.add(current);
        current = null;
        break;
      case 46: // Flamethrower
        current = BonusEffect.BURN;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 47: // Flash FLAG - Accuracy
        current = BonusEffect.CONFUSE;
        current.chance = .4;
        bonuseffects.add(current);
        current = null;
        break;
      case 49: // Focus Energy Flag - Crit
        current = BonusEffect.ATTACK;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = .5;
        bonuseffects.add(current);
        current = null;
        current = BonusEffect.SPECATTACK;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = .5;
        bonuseffects.add(current);
        current = null;
        break;
      case 52: // Glare
        current = BonusEffect.PARALYZE;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 53: // Growl
        current = BonusEffect.ATTACK;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 54: // Growth
        current = BonusEffect.ATTACK;
        current.target = Target.SELF;
        current.power = 1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        current = BonusEffect.SPECATTACK;
        current.target = Target.SELF;
        current.power = 1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 57: // Harden
        current = BonusEffect.DEFENSE;
        current.target = Target.SELF;
        current.power = 1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 59: // Headbutt
        current = BonusEffect.FLINCH;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 65: // Hyper Fang
        current = BonusEffect.FLINCH;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 66: // Hypnosis
        current = BonusEffect.SLEEP;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 67: // Ice Beam
        current = BonusEffect.FREEZE;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 68: // Ice Punch
        current = BonusEffect.FREEZE;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 71: // Kinesis FLAG - Accuracy
        current = BonusEffect.SPECATTACK;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 72: // Leech Life
        current = BonusEffect.HEAL;
        current.target = Target.SELF;
        current.chance = 1.0;
        current.percent = .2;
        bonuseffects.add(current);
        current = null;
        break;
      case 73: // Leech Seed
        current = BonusEffect.LEECH;
        current.target = Target.SELF;
        current.chance = 1.0;
        current.percent = .2;
        bonuseffects.add(current);
        current = null;
        current = BonusEffect.LEECH;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        current.percent = .2;
        bonuseffects.add(current);
        current = null;
        break;
      case 74: // Leer
        current = BonusEffect.DEFENSE;
        current.target = Target.ENEMY;
        current.power = -1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 75: // Lick
        current = BonusEffect.PARALYZE;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 77: // Lovely Kiss
        current = BonusEffect.SLEEP;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 78: // Low Kick
        current = BonusEffect.FLINCH;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 79: // Meditate
        current = BonusEffect.ATTACK;
        current.target = Target.SELF;
        current.power = 1;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 80: // Mega Drain
        current = BonusEffect.HEAL;
        current.target = Target.SELF;
        current.chance = 1.0;
        current.percent = .5;
        bonuseffects.add(current);
        current = null;
        break;
      case 85: // Minimize FLAG - Evasion
        current = BonusEffect.DEFENSE;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = .5;
        bonuseffects.add(current);
        current = null;
        current = BonusEffect.SPECDEFENSE;
        current.target = Target.SELF;
        current.power = 2;
        current.chance = .5;
        bonuseffects.add(current);
        current = null;
        break;
      case 91: // Petal Dance
        current = BonusEffect.CONFUSE;
        current.target = Target.SELF;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 93: // Poison Gas
        current = BonusEffect.POISON;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 94: // Poison Sting
        current = BonusEffect.POISON;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 95: // Poisonpowder
        current = BonusEffect.POISON;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 97: // Psybeam
        current = BonusEffect.CONFUSE;
        current.target = Target.ENEMY;
        current.chance = .1;
        bonuseffects.add(current);
        current = null;
        break;
      case 98: // Psychic
        current = BonusEffect.SPECDEFENSE;
        current.target = Target.ENEMY;
        current.chance = .1;
        current.power = -1;
        bonuseffects.add(current);
        current = null;
        break;
      case 101: // Rage
        current = BonusEffect.ATTACK;
        current.target = Target.SELF;
        current.chance = 1;
        current.power = 1;
        bonuseffects.add(current);
        current = null;
        break;
      case 104: // Recover
        current = BonusEffect.HEAL;
        current.target = Target.SELF;
        current.percent = .5;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 106: // Rest
        current = BonusEffect.SLEEP;
        current.target = Target.SELF;
        current.chance = 1.0;
        current.power = 2; // Don't know if i'll need this..
        bonuseffects.add(current);
        current = null;
        current = BonusEffect.HEAL;
        current.target = Target.SELF;
        current.chance = 1.0;
        current.percent = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      case 108: // Rock Slide
        current = BonusEffect.FLINCH;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 110: // Rolling Kick
        current = BonusEffect.FLINCH;
        current.target = Target.ENEMY;
        current.chance = .3;
        bonuseffects.add(current);
        current = null;
        break;
      case 111: // Sand-Attack FLAG : Accuracy
        current = BonusEffect.SPEED;
        current.target = Target.ENEMY;
        current.chance = 1.0;
        bonuseffects.add(current);
        current = null;
        break;
      }
    }
  }
}