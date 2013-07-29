package org.jpokemon.action;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.StoreActivity;
import org.jpokemon.item.Store;
import org.jpokemon.trainer.Player;

public class StoreAction extends Action {
  public StoreAction(String data) {
    super(data);
  }

  @Override
  public void execute(Player player) {
    int storeNumber = Integer.parseInt(getData());

    Store store = Store.get(storeNumber);

    if (store != null) {
      PlayerManager.addActivity(player, new StoreActivity(store));
    }
  }
}