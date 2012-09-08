package jpkmn.map;

import jpkmn.Constants;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaManager {
  public static Area get(int id) {
    return _areas[id - 1];
  }

  private static boolean init() {
    _areas = new Area[Constants.AREANUMBER];

    for (int i = 0; i < Constants.AREANUMBER; i++) {
      _areas[i] = new Area(i + 1);
    }

    return true;
  }

  private static Area[] _areas;
  protected static boolean _ready = init();
}