package org.jpokemon.action;

import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;
import org.jpokemon.upgrade.UpgradeActivity;

public class UpgradeAction extends Action {
  public UpgradeAction(String data) {
    super(data);
  }

  @Override
  public void execute(Player player) throws LoadException {
    ActivityTracker.setActivity(player, new UpgradeActivity());
  }
}