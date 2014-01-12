package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public class MapActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    return new MapAction(options);
  }
}

class MapAction implements Action {
  private String mapId;

  public MapAction(String mapId) {
    this.mapId = mapId;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    System.out.println("Execute map action " + mapId + " for player: " + player.id());
  }
}