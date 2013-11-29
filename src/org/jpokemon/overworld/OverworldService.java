package org.jpokemon.overworld;

import java.util.HashMap;

import org.jpokemon.server.JPokemonService;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldService implements JPokemonService {
  private HashMap<String, Map> maps = new HashMap<String, Map>();

  @Override
  public void login(Player player) {
    String mapId = player.getLocation().getMap();

    if (mapId == null) {
      player.getLocation().setMap(mapId = "house");
    }

    Map map = maps.get(mapId);
    if (map == null) {
      maps.put(mapId, map = new Map(mapId));
    }

    JSONObject json = new JSONObject();
    try {
      json.put("action", "overworld:login");
      json.put("spriteheight", 56);
      json.put("spritewidth", 48);
      json.put("image", "male_protagonist");
      json.put("name", player.id());
      json.put("x", player.getLocation().getLeft());
      json.put("y", player.getLocation().getTop());
      json.put("z", map.getEntityZ());
      json.put("map", mapId);
      PlayerManager.pushJson(player, json);

      json.put("action", "overworld:join");
      json.remove("map");
      for (String otherPlayerId : map.getPlayers()) {
        Player otherPlayer = PlayerManager.getPlayer(otherPlayerId);

        json.put("name", otherPlayer.id());
        json.put("x", otherPlayer.getLocation().getLeft());
        json.put("y", otherPlayer.getLocation().getTop());
        PlayerManager.pushJson(player, json);

        json.put("name", player.id());
        json.put("x", player.getLocation().getLeft());
        json.put("y", player.getLocation().getTop());
        PlayerManager.pushJson(otherPlayer, json);
      }
    }
    catch (JSONException e) {
    }

    map.addPlayer(player.id());
  }

  @Override
  public void logout(Player player) {
    String mapId = player.getLocation().getMap();
    Map map = maps.get(mapId);
    map.removePlayer(player.id());

    JSONObject signout = new JSONObject();
    try {
      signout.put("action", "overworld:leave");
      signout.put("name", player.id());
    }
    catch (JSONException e) {
    }

    for (String otherPlayerId : map.getPlayers()) {
      PlayerManager.pushJson(PlayerManager.getPlayer(otherPlayerId), signout);
    }
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    try {
      String method = request.getString("method");

      if (method.equals("move")) {
        move(player, request);
      }
      else if (method.equals("interact")) {
        interact(player, request);
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void move(Player player, JSONObject request) throws JSONException {
    String direction = request.getString("direction");
    Location location = player.getLocation();
    Map map = maps.get(location.getMap());

    int nextLeft = location.getLeft();
    int nextTop = location.getTop();

    if ("left".equals(direction) && nextLeft > 0) {
      nextLeft--;
    }
    else if ("right".equals(direction) && nextLeft < map.getWidth() - 1) {
      nextLeft++;
    }
    else if ("up".equals(direction) && nextTop > 0) {
      nextTop--;
    }
    else if ("down".equals(direction) && nextTop < map.getHeight() - 1) {
      nextTop++;
    }
    else {
      return;
    }

    Entity entityAtNext = map.getEntityAt(nextLeft, nextTop);
    if (entityAtNext != null && entityAtNext.isSolid()) { return; }

    // TODO - handle regions

    location.setTop(nextTop);
    location.setLeft(nextLeft);

    JSONObject move = new JSONObject();
    try {
      move.put("action", "overworld:move");
      move.put("name", player.id());
      move.put("direction", direction);
      move.put("x", nextLeft);
      move.put("y", nextTop);
    }
    catch (JSONException e) {
    }

    for (String playerId : map.getPlayers()) {
      PlayerManager.pushJson(PlayerManager.getPlayer(playerId), move);
    }
  }

  public void interact(Player player, JSONObject request) {

  }

  @Override
  public JSONObject load(JSONObject request, Player player) {
    return null;
  }
}