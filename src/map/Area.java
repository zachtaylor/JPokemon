package map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.Player;

public abstract class Area {
  public Area(String name) {
    _name = name;
    _neighbors = new ArrayList<Area>();
    _buildings = new ArrayList<Building>();
    _requirement = null;

    AreaManager.registerArea(this);
  }

  public String getName() {
    return _name;
  }

  public List<Area> getNeighbors() {
    return _neighbors;
  }

  public void addNeighbor(Area a) {
    if (_neighbors.contains(a)) return;
    _neighbors.add(a);
  }

  public List<Building> getBuildings() {
    return _buildings;
  }

  public void setRequirement(AreaRequirement ar) {
    _requirement = ar;
  }

  public boolean allowPass(Player p) {
    return _requirement == null ? true : _requirement.allowPass(p);
  }

  private String _name;
  private List<Area> _neighbors;
  private AreaRequirement _requirement;
  private List<Building> _buildings;
}
