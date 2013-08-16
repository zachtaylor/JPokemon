package org.jpokemon.overworld;

import java.util.HashMap;
import java.util.Map;

import org.jpokemon.action.ActionSet;

public class Entity {
  private String name;
  private Location lineOfSight;
  private Map<String, ActionSet> actionSets = new HashMap<String, ActionSet>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Location getLineOfSight() {
    return lineOfSight;
  }

  public void setLineOfSight(Location lineOfSight) {
    this.lineOfSight = lineOfSight;
  }

  public ActionSet getActionSet(String s) {
    return actionSets.get(s);
  }

  public ActionSet putActionSet(String s, ActionSet actionSet) {
    return actionSets.put(s, actionSet);
  }
}