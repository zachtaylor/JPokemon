package org.jpokemon.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jpokemon.overworld.Entity;
import org.jpokemon.overworld.Location;
import org.jpokemon.overworld.Map;
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

    Map map = null;
    if (maps.get(mapId) == null) {
      map = new Map(mapId);
      maps.put(mapId, map);
      players.put(map, new ArrayList<Player>());
    }
    else {
      map = maps.get(mapId);
    }

    JSONObject json = new JSONObject();
    try {
      json.put("action", "overworld");
      json.put("spriteheight", 56);
      json.put("spritewidth", 48);
      json.put("image", "male_protagonist");
      json.put("z", map.getEntityZ());

      json.put("x", player.getLocation().getLeft());
      json.put("y", player.getLocation().getTop());
      json.put("login", player.getName());
      json.put("map", mapId);
      PlayerManager.pushJson(player, json);
      json.remove("login");
      json.remove("map");

      for (Player otherPlayer : players.get(map)) {
        json.put("add", otherPlayer.getName());
        json.put("x", otherPlayer.getLocation().getLeft());
        json.put("y", otherPlayer.getLocation().getTop());
        PlayerManager.pushJson(player, json);

        json.put("add", player.getName());
        json.put("x", player.getLocation().getLeft());
        json.put("y", player.getLocation().getTop());
        PlayerManager.pushJson(otherPlayer, json);
      }
    }
    catch (JSONException e) {
    }

    players.get(map).add(player);
    return true;
  }

  @Override
  public boolean onRemove(Player player) {
    String mapId = player.getLocation().getMap();
    Map map = maps.get(mapId);
    players.get(map).remove(player);

    JSONObject signout = new JSONObject();
    try {
      signout.put("action", "overworld");
      signout.put("leave", player.getName());
    }
    catch (JSONException e) {
    }
    for (Player otherPlayer : players.get(map)) {
      PlayerManager.pushJson(otherPlayer, signout);
    }

    return true;
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

      if ("left".equals(direction) && next.getLeft() > 0) {
        next.setLeft(next.getLeft() - 1);
      }
      else if ("right".equals(direction) && next.getLeft() < map.getWidth() - 1) {
        next.setLeft(next.getLeft() + 1);
      }
      else if ("up".equals(direction) && next.getTop() > 0) {
        next.setTop(next.getTop() - 1);
      }
      else if ("down".equals(direction) && next.getTop() < map.getHeight() - 1) {
        next.setTop(next.getTop() + 1);
      }
      else {
        return;
      }

      Entity entityAtNext = map.getEntityAt(next.getLeft(), next.getTop());
      if (entityAtNext != null && entityAtNext.isSolid()) { return; }
      // TODO - handle regions
      location.setTop(next.getTop());
      location.setLeft(next.getLeft());

      JSONObject move = new JSONObject("{action:'overworld', move:'" + direction + "', name:'" + player.getName()
          + "', x:" + location.getLeft() + ", y:" + location.getTop() + "}");

      for (Player p : players.get(map)) {
        PlayerManager.pushJson(p, move);
      }
    }
  }
}