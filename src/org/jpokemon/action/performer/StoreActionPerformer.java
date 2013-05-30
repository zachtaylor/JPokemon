package org.jpokemon.action.performer;

import org.jpokemon.manager.LoadException;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.component.StoreActivity;
import org.jpokemon.map.store.Store;
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