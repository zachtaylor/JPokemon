package jpkmn.map;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jpkmn.game.base.AIInfo;
import jpkmn.game.base.ConnectionInfo;
import jpkmn.game.base.SpawnInfo;
import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.Pokemon;

/**
 * Represents a game area where the player can "be." Areas can contain
 * buildings, and may have water applied to them. Water usage is always
 * optional for the player.
 * 
 * @author Zach
 */
public class Area {
  public final int id;

  public Area(int areaNumber) {
    id = areaNumber;

    _spawner = SpawnInfo.getSpawner(areaNumber);
    _trainers = AIInfo.getAIForArea(areaNumber);
    _neighbors = ConnectionInfo.getConnectionMap(areaNumber);

    // Replace with areainfo
    _name = mapNumberToName();
    _center = mapNumberToCenter();
    _events = new ArrayList<Event>();
    _water = _spawner == null ? false : _spawner.spawn("oldrod") != null;
  }

  public String name() {
    return _name;
  }

  public Trainer getTrainer(int trainerID) {
    AIInfo targetInfo = null;

    for (AIInfo curInfo : _trainers) {
      if (curInfo.getNumber() == trainerID) {
        targetInfo = curInfo;
        break;
      }
    }

    if (targetInfo == null) return null;

    return new Trainer(targetInfo.getType(), targetInfo.getName(),
        targetInfo.getCash(), targetInfo.getNumber());
  }

  public List<AIInfo> trainers() {
    return _trainers;
  }

  public AreaConnection neighbor(Direction d) {
    if (_neighbors == null) return null;

    return _neighbors.get(d);
  }

  public boolean center() {
    return _center;
  }

  public boolean water() {
    return _water;
  }

  public Pokemon spawn(String tag) {
    if (_spawner == null) return null;

    return _spawner.spawn(tag);
  }

  private String mapNumberToName() {
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
    case 12:
      return "Route-4";
    case 13:
      return "Cerulean City";
    case 14:
      return "Route-24";
    case 15:
      return "Route-25";
    case 16:
      return "Route-9";
    case 17:
      return "Route-5";
    case 18:
      return "Saffron City";
    case 19:
      return "Route-8";
    case 20:
      return "Lavender Town";
    case 21:
      return "Route-10";
    case 22:
      return "Route-6";
    case 23:
      return "Vermilion City";
    case 24:
      return "Route-7";
    case 25:
      return "Celadon City";
    case 26:
      return "Route-11";
    case 27:
      return "Diglett's Cave";
    case 28:
      return "Route-12";
    case 29:
      return "Route-13";
    case 30:
      return "Route-14";
    case 31:
      return "Route-15";
    case 32:
      return "Fuchsia City";
    case 33:
      return "Route-18";
    case 34:
      return "Route-17 (Bike Path)";
    case 35:
      return "Route-16";
    case 36:
      return "Route-19";
    case 37:
      return "Route-20 (east)";
    case 38:
      return "Seafoam Islands 1F";
    case 39:
      return "Seafoam Islands B1F";
    case 40:
      return "Seafoam Islands B2F";
    case 41:
      return "Seafoam Islands B3F";
    case 42:
      return "Seafoam Islands B4F";
    case 43:
      return "Route-20 (west)";
    case 44:
      return "Cinnabar Island";
    case 45:
      return "Route-21";

    default:
      return "FakeName";
    }
  }

  private boolean mapNumberToCenter() {
    switch (id) {
    case 1:
    case 3:
    case 7:
    case 13:
    case 18:
    case 20:
    case 23:
    case 25:
    case 32:
    case 44:
      return true;
    default:
      return false;
    }
  }

  private String _name;
  private boolean _water;
  private boolean _center;
  private List<Event> _events;
  private List<AIInfo> _trainers;
  private PokemonSpawner _spawner;
  private Map<Direction, AreaConnection> _neighbors;
}