package jpkmn.map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;

/**
 * Represents a game area where the player can "be." Areas can contain
 * buildings, and may have water applied to them. Water usage is always
 * optional for the player.
 * 
 * @author Zach
 */
public abstract class Area {
  public Area(int areaNumber) {
    id = areaNumber;

    _gym = -1;
    _rival = -1;

    _buildings = new ArrayList<Building>();
    _neighbors = new ArrayList<AreaConnection>();
  }

  public void name(String s) {
    _name = s;
  }

  public String name() {
    return _name;
  }

  public List<Building> buildings() {
    return _buildings;
  }

  public void buildings(Building b) {
    if (!_buildings.contains(b)) _buildings.add(b);
  }

  public List<AreaConnection> neighbors() {
    return _neighbors;
  }

  public AreaConnection neighbors(int n) {
    return _neighbors.get(n);
  }

  public void connect(Area a) {
    if (id < a.id) {
      _neighbors.add(new AreaConnection(a.id));
      a._neighbors.add(new AreaConnection(id));
    }
  }

  public void water(Water w) {
    _water = w;
  }

  public Water water() {
    return _water;
  }

  public Pokemon fish(String pole) {
    return _water == null ? null : _water.spawn(pole);
  }

  public int gym() {
    return _gym;
  }

  public void gym(int number) {
    _gym = number;
  }

  public void rival(int num) {
    _rival = num;
  }

  public int rival() {
    return _rival;
  }

  public final int id;

  protected String _name;
  protected Water _water;
  protected int _rival, _gym;
  protected List<Building> _buildings;
  protected List<AreaConnection> _neighbors;
}