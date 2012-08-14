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

    // Pallet - Route 1
    _areas[0].connect(Direction.NORTH, _areas[10]);
    _areas[10].connect(Direction.SOUTH, _areas[0]);

    // Route 1 - Viridian City
    _areas[10].connect(Direction.NORTH, _areas[1]);
    _areas[1].connect(Direction.SOUTH, _areas[10]);

    // Viridian City - Route 2
    _areas[1].connect(Direction.NORTH, _areas[11]);
    _areas[1].connect(Direction.SOUTH, _areas[11]);

    // Viridian City - Route 22
    _areas[1].connect(Direction.WEST, _areas[12]);
    _areas[12].connect(Direction.EAST, _areas[13]);

    // Route 2 - Viridian Forest
    _areas[12].connect(Direction.NORTH, _areas[13]);
    _areas[13].connect(Direction.SOUTH, _areas[12]);

    // Viridan Forest - Pewter City
    _areas[13].connect(Direction.NORTH, _areas[2]);
    _areas[2].connect(Direction.SOUTH, _areas[13]);

    return true;
  }

  private static Area[] _areas;
  protected static boolean _ready = init();
}