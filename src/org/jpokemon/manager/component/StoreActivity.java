package org.jpokemon.manager.component;

import org.jpokemon.manager.Activity;
import org.jpokemon.manager.JPokemonService;
import org.jpokemon.map.store.Store;
import org.jpokemon.trainer.Player;

public class StoreActivity implements Activity {
  public StoreActivity(Store store) {
    _store = store;
  }

  @Override
  public String getName() {
    return "store";
  }

  @Override
  public JPokemonService getHandler() {
    return StoreService.getInstance();
  }

  @Override
  public StoreServer getServer(Player player) {
    return new StoreServer(player);
  }

  public Store getStore() {
    return _store;
  }

  private Store _store;
}