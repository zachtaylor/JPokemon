package org.jpokemon.battle.turn;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.item.Item;
import org.jpokemon.item.ItemType;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.WildTrainer;

public class ItemTurn extends Turn {
  public ItemTurn(Battle b, Slot user, Slot target, Item item, int targetIndex) {
    super(b, user, target);

    _targetIndex = targetIndex;
    _item = item;

    battle().log(slot().trainer().id() + " used " + item.name());
  }

  @Override
  public void execute() {
    Pokemon target = target().party().get(_targetIndex);

    if (_item.type() == ItemType.MACHINE)
      battle().log("Machines aren't allowed in battle!");
    else if (_item.type() == ItemType.STONE)
      battle().log("Stones aren't allowed in battle!");
    else if (_item.type() == ItemType.POTION)
      _item.effect(target);
    else if (_item.type() == ItemType.BALL) {
      if (!(target().trainer() instanceof WildTrainer))
        battle().log("Cannot use a ball against " + target.name() + "!");
      else if (_item.effect(target)) {
        if (!slot().trainer().add(target))
          battle().log("No room for " + target.name());
        else {
          target().party().remove(target);
          battle().log(target.name() + " was caught!");
        }
      }
      else
        battle().log(target.name() + "broke free!");
    }
  }

  @Override
  public boolean reAdd() {
    return false;
  }

  @Override
  public int compareTo(Turn turn) {
    if (turn instanceof AttackTurn) return -1;
    if (turn instanceof ItemTurn) return 0;

    return 1;
  }

  private int _targetIndex;
  private Item _item;
}