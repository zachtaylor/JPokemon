package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.trainer.Player;

public class ItemActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    String[] optionsSplit = options.split(" ");
    int itemId = Integer.parseInt(optionsSplit[0]);
    int quantity = Integer.parseInt(optionsSplit[1]);

    return new ItemAction(itemId, quantity);
  }
}

class ItemAction implements Action {
  int itemId, quantity;

  public ItemAction(int itemId, int quantity) {
    this.itemId = itemId;
    this.quantity = quantity;
  }

  public void execute(Player player) {
    player.item(itemId).add(quantity);
  }
}