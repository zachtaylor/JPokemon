package org.jpokemon.action.performer;

import org.jpokemon.item.Store;
import org.jpokemon.manager.LoadException;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.activity.StoreActivity;
import org.jpokemon.trainer.Player;

public class StoreActionPerformer extends AbstractActionPerformer {
  public StoreActionPerformer(String data) {
    super(data);
  }

  @Override
  public void execute(Player player) throws LoadException {
    int storeNumber = Integer.parseInt(getData());

    Store store = Store.get(storeNumber);

    if (store != null) {
      PlayerManager.setActivity(player, new StoreActivity(store));
    }
  }
}