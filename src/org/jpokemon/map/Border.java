package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;

public class Border {

  public int getArea() {
    return area;
  }

  public void setArea(int a) {
    area = a;
  }

  public int getNext() {
    return next;
  }

  public void setNext(int n) {
    next = n;
  }

  public void addRequirement(BorderRequirement br) {
    Requirement r = new Requirement(br.getType(), br.getData());

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

  private int area, next;
  private List<Requirement> _requirements = new ArrayList<Requirement>();
}