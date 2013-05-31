package org.jpokemon.action.performer;

import org.jpokemon.trainer.Event;
import org.jpokemon.trainer.Player;

public class EventActionPerformer extends AbstractActionPerformer {
  public EventActionPerformer(String data) {
    super(data);
  }

  public void execute(Player player) {
    Event event = Event.get(Integer.parseInt(getData()));
    player.record().putEvent(event.getNumber());
  }
}