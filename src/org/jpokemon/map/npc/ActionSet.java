package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.map.Requirement;
import org.jpokemon.trainer.Player;

public class ActionSet {
  public List<Action> actions() {
    return _actions;
  }

  public String getOption() {
    return _option;
  }

  public void setOption(String o) {
    _option = o;
  }

  public void addAction(Action a) {
    _actions.add(a);
  }

  public void execute(Player p) {
    if (!isOkay(p))
      return;

    for (Action action : _actions)
      action.execute(p);
  }

  public void requirements(List<List<Requirement>> requirements) {
    _requirements = requirements;
  }

  public List<List<Requirement>> requirements() {
    return _requirements;
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

  private String _option;
  private List<Action> _actions = new ArrayList<Action>();
  private List<List<Requirement>> _requirements = new ArrayList<List<Requirement>>();
}