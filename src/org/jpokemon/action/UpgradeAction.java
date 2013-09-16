package org.jpokemon.action;

import org.jpokemon.pokemon.UpgradeActivity;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.trainer.Player;

public class UpgradeAction extends Action {
  @Override
  public void execute(Player player) {
    PlayerManager.addActivity(player, new UpgradeActivity());
  }
}