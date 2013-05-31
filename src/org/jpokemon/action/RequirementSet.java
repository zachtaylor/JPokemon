package org.jpokemon.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jpokemon.trainer.Player;

public class RequirementSet implements Iterable<Requirement> {
  public static final String XML_NODE_NAME = "requirementset";

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

  @Override
  public Iterator<Requirement> iterator() {
    return _data.iterator();
  }

  private List<Requirement> _data = new ArrayList<Requirement>();
}