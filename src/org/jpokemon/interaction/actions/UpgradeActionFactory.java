package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.pokemon.UpgradeActivity;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.trainer.Player;

public class UpgradeActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    return new UpgradeAction();
  }
}

class UpgradeAction implements Action {
  @Override
  public void execute(Player player) {
    PlayerManager.addActivity(player, new UpgradeActivity());
  }
}