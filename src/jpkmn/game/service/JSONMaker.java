package jpkmn.game.service;

import jpkmn.game.player.Player;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.Direction;

import org.jpokemon.pokemon.stat.StatType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONMaker {
  public static JSONObject make(Area area) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", area.id());
    json.put("name", area.name());
    json.put("hasWater", area.water());
    json.put("hasCenter", area.center());
    json.put("hasGrass", area.grass());

    for (Direction d : Direction.values()) {
      AreaConnection con = area.neighbor(d);
      if (con == null)
        json.put(d.name(), "");
      else
        json.put(d.name(), con.next().name());
    }

    json.put("events", area.eventsToJSON());
    json.put("trainers", area.trainersToJSON());

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
      stat.put("value", p.getStat(st).cur());
      stat.put("points", p.getStat(st).points());

      stats.put(stat);
    }
    json.put("stats", stats);

    return json;
  }

  public static JSONObject make(Player p) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("id", p.id());
    json.put("name", p.name());
    json.put("cash", p.cash());
    json.put("badge", p.badge());
    json.put("party", make(p.party()));

    return json;
  }

  private static JSONArray make(Iterable<Pokemon> p) throws JSONException {
    JSONArray json = new JSONArray();

    for (Pokemon pkmn : p)
      json.put(make(pkmn));

    return json;
  }
}