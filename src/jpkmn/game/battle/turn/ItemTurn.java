package jpkmn.game.battle.turn;

import jpkmn.game.battle.Slot;
import jpkmn.game.battle.Target;
import jpkmn.game.item.Item;
import jpkmn.game.item.ItemType;
import jpkmn.game.player.TrainerType;
import jpkmn.game.pokemon.Pokemon;

public class ItemTurn extends AbstractTurn {
  public ItemTurn(Slot user, Item item, int targetID) {
    super(user);

    _targetID = targetID;
    _item = item;

    _messages.add(_user.trainer().name() + " used " + item.name());
  }

  public void execute() {
    if (needSwap()) {
      executeForcedSwap();
      return;
    }

    Pokemon target;
    if (_item.target() == Target.SELF)
      target = _user.party().get(_targetID);
    else
      target = _user.battle().get(_targetID).party().get(0);

    if (_item.type() == ItemType.MACHINE)
      _messages.add("Machines aren't allowed in battle!");
    else if (_item.type() == ItemType.STONE)
      _messages.add("Stones aren't allowed in battle!");
    else if (_item.type() == ItemType.POTION)
      _item.effect(target);
    else if (_item.type() == ItemType.BALL) {
      if (target.trainer().type() != TrainerType.WILD)
        _messages.add("Cannot use a ball against " + target.name() + "!");
      else if (_item.effect(target)) {
        if (!_user.trainer().add(target))
          _messages.add("No room for " + target.name());
        else
          _messages.add(target.name() + " was caught!");
      }
      else
        _messages.add(target.name() + "broke free!");
    }
  }

  @Override
  public int compareTo(AbstractTurn turn) {
    if (turn.needSwap()) {
      if (needSwap())
        return 0;
      return 1;
    }

    if (turn instanceof AttackTurn)
      return -1;
    if (turn instanceof ItemTurn)
      return 0;
    return 1;
  }

  private int _targetID;
  private Item _item;
}