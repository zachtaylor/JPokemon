package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

public class Area {
  public Area(int id) {
    _id = id;

    AreaInfo info = AreaInfo.get(id);
    _name = info.getName();
  }

  public String name() {
    return _name;
  }

  public List<Border> borders() {
    return _borders;
  }

  public void addBorder(Border b) {
    if (b == null || _borders.contains(b))
      return;

    _borders.add(b);
  }

  public void removeBorder(Border b) {
    if (b == null || !_borders.contains(b))
      return;

    _borders.remove(b);
  }

  public boolean equals(Object o) {
    if (o instanceof Area)
      return ((Area) o)._id == _id;

    return false;
  }

  private int _id;
  private String _name;
  private List<Border> _borders = new ArrayList<Border>();
}