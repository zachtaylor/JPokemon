package org.jpokemon.action;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;

public class RequirementSet {
  public void add(Requirement r) {
    _data.add(r);
  }

  public boolean isOkay(Player p) {
    boolean result = true;

    for (Requirement r : _data) {
      if (!r.isOkay(p)) {
        result = false;
      }
    }

    return result;
  }

  private List<Requirement> _data = new ArrayList<Requirement>();
}