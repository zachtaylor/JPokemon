package org.jpokemon.action;

import org.jpokemon.trainer.Event;
import org.jpokemon.trainer.Player;

public class EventAction extends Action {
  public EventAction(String data) {
    super(data);
  }

  public void execute(Player player) {
    Event event = Event.get(Integer.parseInt(getData()));
    player.record().putEvent(event.getNumber());
  }
}