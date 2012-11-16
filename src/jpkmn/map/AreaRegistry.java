package jpkmn.map;

import org.jpokemon.JPokemonConstants;

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
    if (_areas[id - 1] == null) {
      _areas[id - 1] = new Area(id);
    }

    return _areas[id - 1];
  }

  private static Area[] _areas = new Area[JPokemonConstants.AREANUMBER];
}