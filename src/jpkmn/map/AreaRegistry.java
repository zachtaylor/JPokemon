package jpkmn.map;

import jpkmn.Constants;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaRegistry {

  /**
   * Gets a reference to the specified Area
   * 
   * @param id Area's ID
   * @return The Area
   */
  public static Area get(int id) {
    return _areas[id - 1];
  }

  /**
   * Trick to initialize the map on JVM load
   * 
   * @return doesn't matter
   */
  private static Area[] initAreas() {
    Area[] areas = new Area[Constants.AREANUMBER];

    for (int i = 0; i < Constants.AREANUMBER; i++)
      areas[i] = new Area(i + 1);

    return areas;
  }

  private static Area[] _areas = initAreas();
}