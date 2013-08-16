package org.jpokemon.overworld;

import java.util.List;

import org.jpokemon.action.ActionSet;

public class Region {
  private Location location;
  private String trigger;
  private List<ActionSet> actionSets;

  public Location getLocation() {
    return location;
  }

  public void setLocation(Location location) {
    this.location = location;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public List<ActionSet> getActionSets() {
    return actionSets;
  }
}