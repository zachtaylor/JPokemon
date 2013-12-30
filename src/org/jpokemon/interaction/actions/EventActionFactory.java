package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.interaction.ActionFactory;
import org.jpokemon.trainer.Player;

public class EventActionFactory implements ActionFactory {
  @Override
  public Action buildAction(String options) {
    // TODO Auto-generated method stub
    return null;
  }
}

class EventAction implements Action {
  private int eventId;

  public EventAction(int eventId) {
    this.eventId = eventId;
  }

  public void execute(Player player) {
    player.record().putEvent(eventId);
  }
}