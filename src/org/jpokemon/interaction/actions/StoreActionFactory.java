package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.item.Store;
import org.jpokemon.item.StoreActivity;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public class StoreActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    int storeId = Integer.parseInt(options);
    return new StoreAction(storeId);
  }
}

class StoreAction implements Action {
  private int storeId;

  public StoreAction(int storeId) {
    this.storeId = storeId;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    Store store = Store.get(storeId);

    if (store == null) {
      throw new ServiceException("Store undefined: " + storeId);
    }

    PlayerManager.addActivity(player, new StoreActivity(store));
  }
}