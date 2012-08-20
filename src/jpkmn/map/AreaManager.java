package jpkmn.map;

import jpkmn.Constants;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaManager {
  public static void main(String[] args) {
    System.out.println(_areas[0].name());
  }

  public static Area get(int id) {
    return _areas[id];
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