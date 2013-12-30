package org.jpokemon.overworld;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.interaction.ActionSet;

public class Entity {
  private boolean solid;
  protected String name;
  protected Map<String, List<ActionSet>> actionSets = new HashMap<String, List<ActionSet>>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isSolid() {
    return solid;
  }

  public void setSolid(boolean solid) {
    this.solid = solid;
  }

  public List<ActionSet> getActionSets(String trigger) {
    return actionSets.get(trigger);
  }

  public void addActionSet(String trigger, ActionSet actionSet) {
    if (actionSets.get(trigger) == null) {
      actionSets.put(trigger, new ArrayList<ActionSet>());
    }

    actionSets.get(trigger).add(actionSet);
  }
}