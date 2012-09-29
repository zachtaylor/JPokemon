package jpkmn.game.pokemon.move;

import jpkmn.game.base.MoveEffectInfo;
import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Condition;
import jpkmn.game.pokemon.Pokemon;

public class MoveEffect {
  public enum Type {
    ATTACK, DEFENSE, SPECATTACK, SPECDEFENSE, SPEED, BURN, PARALYZE, SLEEP,
    POISON, FREEZE, CONFUSE, WRAP, FLINCH, HEAL, LEECH, KAMIKAZE;

    public static MoveEffect.Type valueOf(int style) {
      if (style < 0 || style > values().length)
        return null;
      return values()[style];
    }
  }

  public MoveEffect(MoveEffectInfo beb) {
    _power = beb.getPower();
    _chance = beb.getChance();
    _target = Target.valueOf(beb.getTarget());
    _type = MoveEffect.Type.valueOf(beb.getType());
  }

  public Target target() {
    return _target;
  }

  public MoveEffect.Type type() {
    return _type;
  }

  public void effect(Pokemon p) {
    if (_chance < Math.random())
      return; // didn't hit

    if (_type == MoveEffect.Type.ATTACK)
      p.stats.atk.effect(_power);
    else if (_type == MoveEffect.Type.DEFENSE)
      p.stats.def.effect(_power);
    else if (_type == MoveEffect.Type.SPECATTACK)
      p.stats.stk.effect(_power);
    else if (_type == MoveEffect.Type.SPECDEFENSE)
      p.stats.sdf.effect(_power);
    else if (_type == MoveEffect.Type.SPEED)
      p.stats.spd.effect(_power);
    else if (_type == MoveEffect.Type.BURN)
      p.condition.addIssue(Condition.Issue.BURN);
    else if (_type == MoveEffect.Type.PARALYZE)
      p.condition.addIssue(Condition.Issue.PARALYZE);
    else if (_type == MoveEffect.Type.SLEEP)
      p.condition.addIssue(Condition.Issue.SLEEP);
    else if (_type == MoveEffect.Type.POISON)
      p.condition.addIssue(Condition.Issue.POISON);
    else if (_type == MoveEffect.Type.FREEZE)
      p.condition.addIssue(Condition.Issue.FREEZE);
    else if (_type == MoveEffect.Type.CONFUSE)
      p.condition.addIssue(Condition.Issue.CONFUSE);
    else if (_type == MoveEffect.Type.WRAP)
      p.condition.addIssue(Condition.Issue.WRAP);
    else if (_type == MoveEffect.Type.FLINCH)
      p.condition.addIssue(Condition.Issue.FLINCH);
    else if (_type == MoveEffect.Type.HEAL)
      p.healDamage((int) (p.stats.hp.max() * _power / 1000.0));
    else if (_type == MoveEffect.Type.KAMIKAZE)
      p.takeDamage((int) (p.stats.hp.max() * _power / 1000.0));
  }

  public String toString() {
    return _type + " "+_target;
  }
  
  private int _power;
  private Target _target;
  private double _chance;
  private MoveEffect.Type _type;
}