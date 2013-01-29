package jpkmn.map;

import java.util.HashMap;
import java.util.Map;

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
    if (_areas.get(id) == null)
      _areas.put(id, new Area(id));

    return _areas.get(id);
  }

  private static Map<Integer, Area> _areas = new HashMap<Integer, Area>();
}