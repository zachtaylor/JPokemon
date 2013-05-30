package org.jpokemon.action.performer;

import org.jpokemon.manager.LoadException;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.component.UpgradeActivity;
import org.jpokemon.trainer.Player;

public class UpgradeActionPerformer implements ActionPerformer {
  @Override
  public void execute(Player player) throws LoadException {
    PlayerManager.setActivity(player, new UpgradeActivity());
  }
}