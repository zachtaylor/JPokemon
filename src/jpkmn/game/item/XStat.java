package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.stat.StatType;

public class XStat extends Item {
  public XStat(int power, int quantity, StatType type) {
    super(power, quantity, "X" + type.name());
    _type = type;
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    p.stats.getStat(_type).effect(1);
    return true;
  }

  private StatType _type;
}