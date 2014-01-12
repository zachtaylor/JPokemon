package org.jpokemon.overworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.interaction.ActionSet;

public class Entity {
  protected String name;
  protected boolean solid = true;
  protected Location location;
  protected Map<String, List<ActionSet>> actionSets = new HashMap<String, List<ActionSet>>();

  public String getName() {
    return name;
  }

  public Entity setName(String name) {
    this.name = name;
    return this;
  }

  public boolean isSolid() {
    return solid;
  }

  public Entity setSolid(boolean solid) {
    this.solid = solid;
    return this;
  }

  public Location getLocation() {
    return location;
  }

  public Entity setLocation(Location location) {
    this.location = location;
    return this;
  }

  public List<ActionSet> getActionSets(String trigger) {
    return actionSets.get(trigger);
  }

  public Entity setActionSets(String trigger, List<ActionSet> actionSets) {
    this.actionSets.put(trigger, actionSets);
    return this;
  }

  public Entity addActionSet(String trigger, ActionSet actionSet) {
    if (!this.actionSets.containsKey(trigger)) {
      this.actionSets.put(trigger, new ArrayList<ActionSet>());
    }

    this.actionSets.get(trigger).add(actionSet);
    return this;
  }

  public Entity addAllActionSets(String trigger, List<ActionSet> actionSets) {
    if (!this.actionSets.containsKey(trigger)) {
      this.actionSets.put(trigger, new ArrayList<ActionSet>());
    }

    this.actionSets.get(trigger).addAll(actionSets);
    return this;
  }
}