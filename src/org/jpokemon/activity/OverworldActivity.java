package org.jpokemon.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jpokemon.overworld.Entity;
import org.jpokemon.overworld.Location;
import org.jpokemon.overworld.Map;
import org.jpokemon.provider.OverworldDataProvider;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldActivity implements Activity {
  public static OverworldActivity instance = new OverworldActivity();

  private HashMap<String, Map> maps = new HashMap<String, Map>();
  private HashMap<Map, List<Player>> players = new HashMap<Map, List<Player>>();

  private OverworldActivity() {
  }

  public static OverworldActivity getInstance() {
    return instance;
  }

  @Override
  public boolean onAdd(Player player) {
    String mapId = player.getLocation().getMap();

    if (mapId == null) {
      player.getLocation().setMap(mapId = "house");
    }
    if (maps.get(mapId) == null) {
      Map map = new Map(mapId);
      maps.put(mapId, map);
      players.put(map, new ArrayList<Player>());
    }

    players.get(mapId).add(player);
    PlayerManager.pushJson(player, OverworldDataProvider.generate(player));
    return true;
  }

  @Override
  public boolean onRemove(Player player) {
    String mapId = player.getLocation().getMap();
    players.get(mapId).remove(player);

    return false;
  }

  @Override
  public boolean supportsAction(String action) {
    return true;
  }

  @Override
  public void handleRequest(Player player, JSONObject request) throws JSONException, ServiceException {
    Location location = player.getLocation();
    Map map = maps.get(location.getMap());

    if (request.has("move")) {
      String direction = request.getString("move");

      Location next = location.clone();
      if ("left".equals(direction)) {
        next.setBounds(next.getLeft() - 1, next.getWidth(), next.getTop(), next.getHeight());
      }
      else if ("right".equals(direction)) {
        next.setBounds(next.getLeft() + 1, next.getWidth(), next.getTop(), next.getHeight());
      }
      else if ("up".equals(direction)) {
        next.setBounds(next.getLeft(), next.getWidth(), next.getTop() - 1, next.getHeight());
      }
      else if ("down".equals(direction)) {
        next.setBounds(next.getLeft(), next.getWidth(), next.getTop() + 1, next.getHeight());
      }

      Entity entityAtNext = map.getEntityAt(next.getLeft(), next.getTop());

      if (entityAtNext != null && entityAtNext.isSolid()) { return; }
      // TODO - handle regions

      location.loadXml(next.toXml()); // this is kinda bad...
      PlayerManager.pushJson(player, new JSONObject("{action:'overworld', move:'left', player:'" + player.getName()
          + "', x:" + location.getLeft() + ", y:" + location.getTop() + "}"));
    }
  }
}