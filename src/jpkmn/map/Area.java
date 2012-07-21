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

  public Pokemon water() {
    return _water == null ? null : _water.species();
  }

  public void rivalBattle(int num) {
    _rivalBattle = num;
  }

  public int rivalBattle() {
    return _rivalBattle;
  }

  public final int id;

  protected String _name;
  protected Water _water;
  protected int _rivalBattle = 0;
  protected List<Building> _buildings;
  protected List<AreaConnection> _neighbors;
}