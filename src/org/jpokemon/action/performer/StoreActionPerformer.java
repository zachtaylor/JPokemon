package org.jpokemon.action.performer;

import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.map.store.Store;
import org.jpokemon.map.store.StoreActivity;
import org.jpokemon.service.LoadException;
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
      ActivityTracker.setActivity(player, new StoreActivity(store));
    }
  }
}