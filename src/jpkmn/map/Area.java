package jpkmn.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.game.base.ConnectionInfo;
import jpkmn.game.base.SpawnInfo;
import jpkmn.game.pokemon.Pokemon;

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
    _name = tempMethodToSetName();

    _events = new ArrayList<Event>();
    _buildings = new ArrayList<Building>();
    _neighbors = new HashMap<Direction, AreaConnection>();

    _spawner = SpawnInfo.getSpawner(areaNumber);
    _neighbors = ConnectionInfo.getConnectionMap(areaNumber);
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
    if (_neighbors == null) return null;

    return _neighbors.get(d);
  }

  public boolean water() {
    if (_spawner == null) return false; // temp untill water is added to areainfo
    return _spawner.spawn("oldrod") != null;
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

  public Pokemon spawn(String tag) {
    return _spawner.spawn(tag);
  }

  private String tempMethodToSetName() {
    switch (id) {
    case 1:
      return "Pallet Town";
    case 2:
      return "Route-1";
    case 3:
      return "Viridian City";
    case 4:
      return "Route-2";
    case 5:
      return "Route-22";
    case 6:
      return "Viridian Forest";
    case 7:
      return "Pewter City";
    case 8:
      return "Route-3";
    case 9:
      return "Mt. Moon 1F";
    case 10:
      return "Mt. Moon B1F";
    case 11:
      return "Mt. Moon B2F";
    default:
      return "FakeName";
    }
  }

  public final int id;

  private String _name;
  private boolean _water;
  private int _rival, _gym;
  private List<Event> _events;
  private PokemonSpawner _spawner;
  private List<Building> _buildings;
  private Map<Direction, AreaConnection> _neighbors;
}