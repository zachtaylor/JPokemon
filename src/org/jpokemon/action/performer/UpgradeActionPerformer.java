package org.jpokemon.action.performer;

import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;
import org.jpokemon.upgrade.UpgradeActivity;

public class UpgradeActionPerformer implements ActionPerformer {
  @Override
  public void execute(Player player) throws LoadException {
    ActivityTracker.setActivity(player, new UpgradeActivity());
  }
}