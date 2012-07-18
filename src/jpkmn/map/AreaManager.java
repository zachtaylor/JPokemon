package jpkmn.map;

import jpkmn.Constants;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaManager {
  public static Area get(int id) {
    return _areas[id];
  }

  private static boolean init() {
    _areas = new Area[Constants.CITYNUMBER + Constants.ROUTENUMBER];

    for (int i = 0; i < Constants.AREANUMBER; i++) {
      if (i < Constants.CITYNUMBER)
        _areas[i] = new City(i);
      else
        _areas[i] = new Route(i);
    }

    _areas[0].connect(_areas[10]); // Pallet - Route 1
    _areas[1].connect(_areas[10]); // Viridian City - Route 1
    _areas[1].connect(_areas[11]); // Viridian City - Route 2
    _areas[1].connect(_areas[12]); // Viridian City - Route 22
    _areas[12].connect(_areas[13]); // Route 22 - Viridian Forest
    _areas[2].connect(_areas[13]); // Pewter City - Viridian Forest

    return true;
  }

  private static Area[] _areas;
  protected static boolean _ready = init();
}
