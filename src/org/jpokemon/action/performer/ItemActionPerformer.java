package org.jpokemon.action.performer;

import org.jpokemon.trainer.Player;

public class ItemActionPerformer extends AbstractActionPerformer {
  public ItemActionPerformer(String data) {
    super(data);
  }

  public void execute(Player player) {
    String[] numberAndQuantity = getData().split(" ");
    player.item(Integer.parseInt(numberAndQuantity[0])).add(Integer.parseInt(numberAndQuantity[1]));
  }
}