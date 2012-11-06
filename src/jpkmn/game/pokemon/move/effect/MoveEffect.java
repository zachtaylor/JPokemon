package jpkmn.game.pokemon.move.effect;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.stat.StatType;

public class MoveEffect {
  public MoveEffect(MoveEffectInfo info) {
    _power = info.getPower();
    _chance = info.getChance();
    _target = Target.valueOf(info.getTarget());
    _type = MoveEffectType.valueOf(info.getType());
  }

  public MoveEffectType type() {
    return _type;
  }

  public void effect(Pokemon user, Pokemon enemy) {
    if (_chance < Math.random())
      return; // didn't hit

    MoveEffectType type = type();
    Pokemon target = _target == Target.SELF ? user : enemy;

    if (type.isStatModifier())
      target.getStat(StatType.valueOf(type.toString())).effect(_power);
    else if (type.isConditionModifier())
      target.addIssue(Condition.Issue.valueOf(type.toString()));
    else if (type == MoveEffectType.HEAL)
      target.healDamage((int) (target.maxHealth() * _power / 1000.0));
    else if (type == MoveEffectType.KAMIKAZE)
      target.takeDamage((int) (target.maxHealth() * _power / 1000.0));
    else if (type == MoveEffectType.LEECH) {
      enemy.addIssue(Condition.Issue.SEEDVIC);
      user.addIssue(Condition.Issue.SEEDUSR);
    }
  }

  private int _power;
  private double _chance;
  private Target _target;
  private MoveEffectType _type;
}