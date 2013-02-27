package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;

public class Border {
  public Border(int na) {
    _nextArea = na;
  }

  public int nextArea() {
    return _nextArea;
  }

  public void addRequirement(Requirement r) {
    if (_requirements == null)
      _requirements = new ArrayList<Requirement>();

    _requirements.add(r);
  }

  public boolean isOkay(Player p) {
    if (_requirements == null)
      return true;

    for (Requirement requirement : _requirements)
      if (!requirement.isOkay(p))
        return false;

    return true;
  }

  private int _nextArea;
  private List<Requirement> _requirements;
}