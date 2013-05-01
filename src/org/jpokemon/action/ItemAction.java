package org.jpokemon.action;

import org.jpokemon.trainer.Player;

public class ItemAction extends Action {
  public ItemAction(String data) {
    super(data);
  }

  public void execute(Player player) {
    String[] numberAndQuantity = data().split(" ");
    player.item(Integer.parseInt(numberAndQuantity[0])).add(Integer.parseInt(numberAndQuantity[1]));
  }
}