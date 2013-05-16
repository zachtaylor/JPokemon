package org.jpokemon.map.store;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.ActivityService;
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
  public ActivityService getHandler() {
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