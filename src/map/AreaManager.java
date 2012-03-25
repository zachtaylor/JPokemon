package map;

import java.util.ArrayList;
import java.util.List;

public class AreaManager {

  public static void registerArea(Area a) {
    if (areas.contains(a)) return;
    areas.add(a);
  }
  
  public static Area get(String name) {
    for (Area a : areas) {
      if (a.getName().equals(name))
        return a;
    }
    return null;
  }

  private static List<Area> areas = new ArrayList<Area>();
}
