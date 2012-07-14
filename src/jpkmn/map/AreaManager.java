package jpkmn.map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.Constants;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaManager {
  public static void init() {
    areas = new ArrayList<Area>();

    for (int i = 1; i <= Constants.ROUTENUMBER; i++) {
      areas.add(new Route(i));
    }

    for (int i = 1; i <= Constants.CITYNUMBER; i++) {
      areas.add(new City(i));
    }
  }

  private static List<Area> areas;
}
