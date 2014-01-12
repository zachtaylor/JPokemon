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
      maps.put(mapId, map = TmxMapParser.parse(mapId));
    }

    JSONObject json = new JSONObject();
    try {
      json.put("action", "overworld:login");
      json.put("spriteheight", 56);
      json.put("spritewidth", 42);
      json.put("image", "male-hero-1");
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

  public void move(Player player, JSONObject request) throws JSONException {
    String direction = request.getString("direction");
    Location location = player.getLocation().clone();
    Map map = maps.get(location.getMap());

    if ("left".equals(direction) && location.getLeft() > 0) {
      location.setLeft(location.getLeft() - 1);
    }
    else if ("right".equals(direction) && location.getRight() < map.getWidth()) {
      location.setLeft(location.getLeft() + 1);
    }
    else if ("up".equals(direction) && location.getTop() > 0) {
      location.setTop(location.getTop() - 1);
    }
    else if ("down".equals(direction) && location.getBottom() < map.getHeight()) {
      location.setTop(location.getTop() + 1);
    }
    else {
      return;
    }

    Entity entityAtNext = map.getEntity(location);
    if (entityAtNext != null && entityAtNext.isSolid()) {
      return;
    }
    // TODO - handle regions

    player.setLocation(location);

    JSONObject json = new JSONObject();
    try {
      json.put("action", "overworld:move");
      json.put("name", player.id());
      json.put("direction", direction);
      json.put("x", location.getLeft());
      json.put("y", location.getTop());
    }
    catch (JSONException e) {
    }

    for (String playerId : map.getPlayers()) {
      PlayerManager.pushJson(PlayerManager.getPlayer(playerId), json);
    }
  }

  public void interact(Player player, JSONObject request) {

  }

  @Override
  public JSONObject load(JSONObject request, Player player) {
    return null;
  }
}