package jpkmn.pokemon.move;

import jpkmn.battle.Target;
import jpkmn.pokemon.*;

public enum BonusEffect {
  // @preformat
  ATTACK, DEFENSE, SPECATTACK, SPECDEFENSE, SPEED, BURN, PARALYZE, SLEEP, 
  POISON, FREEZE, CONFUSE, WRAP, FLINCH, HEAL, LEECH, KAMIKAZE;
  // @format

  public int power;
  public double chance, percent;
  public Target target;

  public void effect(Pokemon p) {
    if (Math.random() > chance) {
      jpkmn.Driver.log(BonusEffect.class, this.name()
          + " failed. this.chance = " + chance);
      return; // didn't hit
    }
    jpkmn.Driver.log(BonusEffect.class, this.name()
        + " connected. Target = " + p.name());

    if (this == BonusEffect.ATTACK) {
      p.stats.atk.effect(power);
    }
    else if (this == BonusEffect.DEFENSE) {
      p.stats.def.effect(power);
    }
    else if (this == BonusEffect.SPECATTACK) {
      p.stats.stk.effect(power);
    }
    else if (this == BonusEffect.SPECDEFENSE) {
      p.stats.sdf.effect(power);
    }
    else if (this == BonusEffect.SPEED) {
      p.stats.spd.effect(power);
    }
    else if (this == BonusEffect.BURN) {
      p.condition.addIssue(Condition.Issue.BURN);
    }
    else if (this == BonusEffect.PARALYZE) {
      p.condition.addIssue(Condition.Issue.PARALYZE);
    }
    else if (this == BonusEffect.SLEEP) {
      p.condition.addIssue(Condition.Issue.SLEEP);
    }
    else if (this == BonusEffect.POISON) {
      p.condition.addIssue(Condition.Issue.POISON);
    }
    else if (this == BonusEffect.FREEZE) {
      p.condition.addIssue(Condition.Issue.FREEZE);
    }
    else if (this == BonusEffect.CONFUSE) {
      p.condition.addIssue(Condition.Issue.CONFUSE);
    }
    else if (this == BonusEffect.WRAP) {
      p.condition.addIssue(Condition.Issue.WRAP);
    }
    else if (this == BonusEffect.FLINCH) { 
      p.condition.addIssue(Condition.Issue.FLINCH);
    }
    else if (this == BonusEffect.HEAL) {
      p.healDamage((int) (p.stats.hp.max * percent));
    }
    else if (this == BonusEffect.KAMIKAZE) {
      p.takeDamage((int) (p.stats.hp.max * percent));
    }
  }

  public static BonusEffect valueOf(int style) {
      switch (style) {
          case 0:  return ATTACK;
          case 1:  return DEFENSE;
          case 2:  return SPECATTACK;
          case 3:  return SPECDEFENSE;
          case 4:  return SPEED;
          case 5:  return BURN;
          case 6:  return PARALYZE;
          case 7:  return SLEEP;
          case 8:  return POISON;
          case 9:  return FREEZE;
          case 10: return CONFUSE;
          case 11: return WRAP;
          case 12: return FLINCH;
          case 13: return HEAL;
          case 14: return LEECH;
          case 15: return KAMIKAZE;
          default: return null;
      }
  }
}
