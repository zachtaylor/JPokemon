package org.jpokemon.interaction.actions;

import org.jpokemon.activity.DresserActivity;
import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public class DresserActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    return new DresserAction();
  }
}

class DresserAction implements Action {
  @Override
  public void execute(Player player) throws ServiceException {
    PlayerManager.addActivity(player, new DresserActivity());
  }
}
