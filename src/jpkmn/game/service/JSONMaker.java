package jpkmn.game.service;

import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.Direction;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONMaker {
  public static JSONObject make(Area area) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", area.id);
    json.put("name", area.name());
    json.put("buildings", area.buildings());
    json.put("hasWater", area.water());

    for (Direction d : Direction.values()) {
      AreaConnection con = area.neighbor(d);
      if (con == null)
        json.put(d.name(), "");
      else
        json.put(d.name(), con.next().name());
    }

    return json;
  }
}