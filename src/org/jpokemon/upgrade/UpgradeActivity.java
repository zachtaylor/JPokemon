package org.jpokemon.upgrade;

import org.jpokemon.activity.Activity;
import org.jpokemon.trainer.Player;

public class UpgradeActivity implements Activity {
  @Override
  public String getName() {
    return "upgrade";
  }

  @Override
  public UpgradeService getHandler() {
    return UpgradeService.getInstance();
  }

  @Override
  public UpgradeServer getServer(Player player) {
    return new UpgradeServer(player);
  }
}