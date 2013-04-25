package org.jpokemon.battle.slot;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class SlotEffect {
  public SlotEffect(SlotEffectType t, Slot user, Slot target) {
    _type = t;
    _user = user;
    _target = target;

    if (_type.hasFiniteDuration())
      _time = JPokemonConstants.SHIELD_ROUND_DURATION;
    else
      _time = 0;
  }

  public String name() {
    return _type.name();
  }

  public double damageModifier(Move m) {
    MoveStyle style = m.style();

    if (_type == SlotEffectType.INVULERNABLE)
      return (1 - JPokemonConstants.SHIELD_REDUCTION_MODIFIER);
    if (style == MoveStyle.PHYSICAL && _type == SlotEffectType.PHYSICAL_SHIELD)
      return (1 - JPokemonConstants.SHIELD_REDUCTION_MODIFIER);
    if (style == MoveStyle.SPECIAL && _type == SlotEffectType.SPECIAL_SHIELD)
      return (1 - JPokemonConstants.SHIELD_REDUCTION_MODIFIER);

    return 1;
  }

  public String apply() {
    switch (_type) {
    case SEED:
      int d = Math.max(1, _target.leader().maxHealth() / 12);
      _target.takeDamage(d);
      _user.leader().healDamage(d);
      return _user.leader().name() + " healed " + d + " from leeching "
          + _target.leader().name();
    default:
      return null;
    }
  }

  /**
   * Reduces the amount of time this effect has remaining.
   * 
   * @return True if this effect persists another turn
   */
  public boolean reduceDuration() {
    _time--;
    return _time != 0;
  }

  public boolean equals(Object o) {
    if (!(o instanceof SlotEffect))
      return false;

    return ((SlotEffect) o)._type == _type;
  }

  private int _time;
  private Slot _user, _target;
  private SlotEffectType _type;
}