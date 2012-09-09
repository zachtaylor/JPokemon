package jpkmn.game.service;

import jpkmn.game.base.AIInfo;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.stat.StatType;
import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.Direction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMaker {
  public static JSONObject make(Area area) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", area.id);
    json.put("name", area.name());
    json.put("hasWater", area.water());
    json.put("hasCenter", area.center());
    json.put("hasGrass", area.spawn(null) != null);

    for (Direction d : Direction.values()) {
      AreaConnection con = area.neighbor(d);
      if (con == null)
        json.put(d.name(), "");
      else
        json.put(d.name(), con.next().name());
    }

    JSONArray trainers = new JSONArray();
    for (AIInfo trainerInfo : area.trainers()) {
      JSONObject data = new JSONObject();

      data.put("name", trainerInfo.getName());
      data.put("id", trainerInfo.getNumber());

      trainers.put(data);
    }
    json.put("trainers", trainers);

    return json;
  }

  public static JSONObject make(Pokemon p) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("name", p.name());
    json.put("number", p.number());

    JSONArray stats = new JSONArray();
    for (StatType st : StatType.values()) {
      // Do it this way to support dynamic stat types

      JSONObject stat = new JSONObject();
      stat.put("name", st.name());
      stat.put("value", p.stats.getStat(st).cur());
      stat.put("points", p.stats.getStat(st).points());

      stats.put(stat);
    }
    json.put("stats", stats);

    return json;
  }
}