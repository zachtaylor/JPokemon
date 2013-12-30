package org.jpokemon.interaction.requirements;

import org.jpokemon.interaction.Requirement;
import org.jpokemon.interaction.RequirementFactory;
import org.jpokemon.trainer.Player;

public class EventRequirementFactory implements RequirementFactory {
  @Override
  public Requirement buildRequirement(String options) {
    return new EventRequirement(Integer.parseInt(options));
  }
}

class EventRequirement implements Requirement {
  private int eventId;

  public EventRequirement(int eventId) {
    this.eventId = eventId;
  }

  @Override
  public boolean isSatisfied(Player player) {
    return player.record().getEvent(Math.abs(eventId)) ^ (eventId < 0); // XOR
  }
}