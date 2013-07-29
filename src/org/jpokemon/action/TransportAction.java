package org.jpokemon.action;

import org.jpokemon.trainer.Player;

public class TransportAction extends Action {
  public TransportAction(String data) {
    super(data);
  }

  public void execute(Player player) {
    String[] areaAndLocation = getData().split(" ");
    player.setArea(Integer.parseInt(areaAndLocation[0]));

    if (areaAndLocation.length > 1)
      ; // When doing coordinates, do that here
  }
}
