package jpkmn.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a game area where the player can "be." Areas can contain
 * buildings, and may have water applied to them. Water usage is always
 * optional for the player.
 * 
 * @author Zach
 */
public class Area {
  public Area(int areaNumber) {
    id = areaNumber;
    _gym = -1;
    _rival = -1;
    _name = "FakeName";

    _water = new Water();
    _events = new ArrayList<Event>();
    _buildings = new ArrayList<Building>();
    _spawnMap = new HashMap<String, PokemonSpawner>();
    _neighbors = new HashMap<Direction, AreaConnection>();
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

  public AreaConnection neighbor(Direction d) {
    return _neighbors.get(d);
  }

  public void connect(Direction d, Area a) {
    _neighbors.put(d, new AreaConnection(a.id));
  }

  public void water(Water w) {
    _water = w;
  }

  public Water water() {
    return _water;
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

  private void add(int num, int flex, int low, int high, String s) {
    _spawnMap.put(s, new PokemonSpawner(num, low, high, flex));
  }

  public final int id;

  private String _name;
  private Water _water;
  private List<Event> _events;
  private int _rival, _gym;
  private List<Building> _buildings;
  private Map<String, PokemonSpawner> _spawnMap;
  private Map<Direction, AreaConnection> _neighbors;
}