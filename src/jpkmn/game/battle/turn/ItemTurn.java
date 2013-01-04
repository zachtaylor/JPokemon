package jpkmn.game.battle.turn;

import jpkmn.game.battle.slot.Slot;
import jpkmn.game.item.Item;
import jpkmn.game.item.ItemType;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.trainer.TrainerType;

public class ItemTurn extends Turn {
  public ItemTurn(Slot user, Item item, int targetID) {
    super(user);

    _itemIndex = targetID;
    _item = item;

    addMessage(slot().trainer().name() + " used " + item.name());
  }

  protected void doExecute() {
    Pokemon target = slot().target().party().get(_itemIndex);

    if (_item.type() == ItemType.MACHINE)
      addMessage("Machines aren't allowed in battle!");
    else if (_item.type() == ItemType.STONE)
      addMessage("Stones aren't allowed in battle!");
    else if (_item.type() == ItemType.POTION)
      _item.effect(target);
    else if (_item.type() == ItemType.BALL) {
      if (slot().target().trainer().type() != TrainerType.WILD)
        addMessage("Cannot use a ball against " + target.name() + "!");
      else if (_item.effect(target)) {
        if (!slot().trainer().add(target))
          addMessage("No room for " + target.name());
        else {
          slot().target().party().remove(target);
          addMessage(target.name() + " was caught!");
        }
      }
      else
        addMessage(target.name() + "broke free!");
    }
  }

  @Override
  public int compareTo(Turn turn) {
    if (turn._needSwap) {
      if (_needSwap)
        return 0;

      return 1;
    }

    if (turn instanceof AttackTurn)
      return -1;
    if (turn instanceof ItemTurn)
      return 0;
    return 1;
  }

  private int _itemIndex;
  private Item _item;
}