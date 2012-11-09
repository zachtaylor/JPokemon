package org.jpokemon.pokemon.move.effect;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.stat.StatType;

public class MoveEffect {
  public void effect(Pokemon user, Pokemon enemy) {
    if (chance < Math.random())
      return; // didn't hit

    MoveEffectType type = MoveEffectType.valueOf(this.type);
    Target target = Target.valueOf(this.target);

    Pokemon pokemon = target == Target.SELF ? user : enemy;

    if (type.isStatModifier())
      pokemon.getStat(StatType.valueOf(type.toString())).effect(power);
    else if (type.isConditionModifier())
      pokemon.addIssue(Condition.Issue.valueOf(type.toString()));
    else if (type == MoveEffectType.HEAL)
      pokemon.healDamage((int) (pokemon.maxHealth() * power / 1000.0));
    else if (type == MoveEffectType.KAMIKAZE)
      pokemon.takeDamage((int) (pokemon.maxHealth() * power / 1000.0));
    else if (type == MoveEffectType.LEECH) {
      enemy.addIssue(Condition.Issue.SEEDVIC);
      user.addIssue(Condition.Issue.SEEDUSR);
    }
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