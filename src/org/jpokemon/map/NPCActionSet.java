package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;

public class NPCActionSet {
  public List<NPCAction> actions() {
    return _actions;
  }

  public void addAction(NPCAction a) {
    _actions.add(a);
  }

  public void execute(Player p) {
    for (NPCAction action : _actions)
      action.execute(p);
  }

  public List<List<Requirement>> requirements() {
    return _requirements;
  }

  public void addRequirementList(List<Requirement> r) {
    _requirements.add(r);
  }

  public boolean isOkay(Player p) {
    boolean result = true;

    for (List<Requirement> opt : _requirements) {
      result = true;

      for (Requirement r : opt) {
        if (!r.isOkay(p)) {
          result = false;
          break;
        }
      }

      if (result)
        break;
    }

    return result;
  }

  private List<NPCAction> _actions = new ArrayList<NPCAction>();
  private List<List<Requirement>> _requirements = new ArrayList<List<Requirement>>();
}