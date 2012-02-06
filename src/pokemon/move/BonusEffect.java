package pokemon.move;

import battle.Target;
import pokemon.*;

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
        + " connected. Target = " + p.name);

    if (this == BonusEffect.ATTACK) {
      p.attack.effect(power);
    }
    else if (this == BonusEffect.DEFENSE) {
      p.defense.effect(power);
    }
    else if (this == BonusEffect.SPECATTACK) {
      p.specattack.effect(power);
    }
    else if (this == BonusEffect.SPECDEFENSE) {
      p.specdefense.effect(power);
    }
    else if (this == BonusEffect.SPEED) {
      p.speed.effect(power);
    }
    else if (this == BonusEffect.BURN) {
      p.status.addEffect(Status.Effect.BURN);
    }
    else if (this == BonusEffect.PARALYZE) {
      p.status.addEffect(Status.Effect.PARALYZE);
    }
    else if (this == BonusEffect.SLEEP) {
      p.status.addEffect(Status.Effect.SLEEP);
    }
    else if (this == BonusEffect.POISON) {
      p.status.addEffect(Status.Effect.POISON);
    }
    else if (this == BonusEffect.FREEZE) {
      p.status.addEffect(Status.Effect.FREEZE);
    }
    else if (this == BonusEffect.CONFUSE) {
      p.status.addEffect(Status.Effect.CONFUSE);
    }
    else if (this == BonusEffect.WRAP) {
      p.status.addEffect(Status.Effect.WRAP);
    }
    else if (this == BonusEffect.FLINCH) { 
      p.status.addEffect(Status.Effect.FLINCH);
    }
    else if (this == BonusEffect.HEAL) {
      p.healDamage((int) (p.health.max * this.percent));
    }
    else if (this == BonusEffect.KAMIKAZE) {
      p.takeDamage((int) (p.health.max * percent));
    }
  }
}
