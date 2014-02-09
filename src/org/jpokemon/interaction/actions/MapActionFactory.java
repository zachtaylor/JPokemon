package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.overworld.Entity;
import org.jpokemon.overworld.Location;
import org.jpokemon.overworld.Map;
import org.jpokemon.overworld.OverworldService;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public class MapActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String s) {
    String[] options = s.split(" ");
    String mapId = options[0];
    String nextMapId = options[1];

    return new MapAction(mapId, nextMapId);
  }
}

class MapAction implements Action {
  private String mapId, nextMapId;

  public MapAction(String mapId, String nextMapId) {
    this.mapId = mapId;
    this.nextMapId = nextMapId;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    OverworldService overworldService = (OverworldService) PlayerManager.getService("overworld");
    Map nextMap = overworldService.getMap(nextMapId);
    Entity nextDoor = nextMap.getEntity(mapId);
    Location nextLocation = nextDoor.getLocation().clone();
    nextLocation.setWidth(1);
    nextLocation.setHeight(1);
    nextLocation.setDirection("down");

    overworldService.teleportPlayer(player, nextLocation);
  }
}
