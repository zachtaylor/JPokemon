package org.jpokemon.action;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.UpgradeActivity;
import org.jpokemon.trainer.Player;

public class UpgradeAction extends Action {
  @Override
  public void execute(Player player) {
    PlayerManager.addActivity(player, UpgradeActivity.getInstance());
  }
}