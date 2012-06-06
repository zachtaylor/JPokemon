package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public class XStat extends Item {
  public XStat(int power, int quantity, Kind kind) {
    super(power, quantity, "X" + kind.formatName());
    _kind = kind;
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    if (_kind == Kind.ATTACK)
      p.stats.atk.effect(1);
    else if (_kind == Kind.SATTACK)
      p.stats.stk.effect(1);
    else if (_kind == Kind.DEFENSE)
      p.stats.def.effect(1);
    else if (_kind == Kind.SDEFENSE)
      p.stats.sdf.effect(1);
    else
      p.stats.spd.effect(1);

    return true;
  }
  
  public enum Kind {
    ATTACK, SATTACK, DEFENSE, SDEFENSE, SPEED;

    private String formatName() {
      if (this == ATTACK || this == DEFENSE || this == SPEED)
        return name().charAt(0) + name().substring(1).toLowerCase();
      else
        return "special-" + name().substring(2).toLowerCase();

    }
  }

  private Kind _kind;
}
