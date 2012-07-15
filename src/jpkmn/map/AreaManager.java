package jpkmn.map;

import jpkmn.Constants;

/**
 * Properly initializes and maintains a list of areas.
 * 
 * @author Zach
 */
public class AreaManager {
  public static void init() {
    areas = new Area[Constants.CITYNUMBER + Constants.ROUTENUMBER];

    for (int i = 0; i < Constants.AREANUMBER; i++) {
      if (i < Constants.CITYNUMBER) areas[i] = new City(i);
      else areas[i] = new Route(i);
    }
    
    areas[0].connect(areas[10]); // Pallet - Route 1
    areas[1].connect(areas[10]); // Viridian City - Route 1
    areas[1].connect(areas[11]); // Viridian City - Route 2
    areas[1].connect(areas[12]); // Viridian City - Route 22
    areas[12].connect(areas[13]); // Route 22 - Viridian Forest
    areas[2].connect(areas[13]); // Pewter City - Viridian Forest
  }
  
  public static Area get(int id) {
    return areas[id];
  }

  private static Area[] areas;
}
