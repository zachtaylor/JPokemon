package org.jpokemon.map;

import java.util.List;

import org.jpokemon.trainer.Player;

public class Border {

  public int nextArea() {
    return _nextArea;
  }

  public void nextArea(int na) {
    _nextArea = na;
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