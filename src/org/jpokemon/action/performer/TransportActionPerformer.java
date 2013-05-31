package org.jpokemon.action.performer;

import org.jpokemon.trainer.Player;

public class TransportActionPerformer extends AbstractActionPerformer {
  public TransportActionPerformer(String data) {
    super(data);
  }

  public void execute(Player player) {
    String[] areaAndLocation = getData().split(" ");
    player.area(Integer.parseInt(areaAndLocation[0]));

    if (areaAndLocation.length > 1)
      ; // When doing coordinates, do that here
  }
}
