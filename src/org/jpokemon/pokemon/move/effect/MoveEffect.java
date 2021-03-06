package org.jpokemon.pokemon.move.effect;

import org.jpokemon.battle.Target;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.slot.SlotEffect;
import org.jpokemon.battle.slot.SlotEffectType;
import org.jpokemon.pokemon.ConditionEffect;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.StatType;

public class MoveEffect {
  public void effect(Slot user, Slot enemy, int damage) {
    if (chance < Math.random())
      return; // didn't hit

    MoveEffectType type = MoveEffectType.valueOf(this.type);
    Target target = Target.valueOf(this.target);

    Pokemon pokemon = target == Target.SELF ? user.leader() : enemy.leader();

    if (type.isStatModifier())
      pokemon.getStat(StatType.valueOf(type.toString())).effect(power);
    else if (type.isConditionModifier())
      pokemon.addConditionEffect(ConditionEffect.valueOf(type.toString()));
    else if (type.isFieldModifier())
      user.addSlotEffect(new SlotEffect(SlotEffectType.valueOf(type.toString()), user, enemy));
    else if (type == MoveEffectType.HEALTH_MOD)
      pokemon.healDamage((int) (pokemon.maxHealth() * power / 1000.0));
  }

  private double chance;
  private int move_number, type, target, power;

  //@preformat
  public int getMove_number() {return move_number;} public void setMove_number(int val) {move_number = val;}
  public int getType() {return type;} public void setType(int val) {type = val;}
  public int getTarget() {return target;} public void setTarget(int val) {target = val;}
  public int getPower() {return power;} public void setPower(int val) {power = val;}
  public double getChance() {return chance;} public void setChance(double val) {chance = val;}
  //@format
}