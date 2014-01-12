package org.jpokemon.overworld;

import java.util.HashMap;
import java.util.List;

import org.jpokemon.interaction.ActionSet;
import org.jpokemon.server.JPokemonService;
import org.jpokemon.server.Message;
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
      player.getLocation().setMap(mapId = "hero-bedroom");
    }

    Map map = maps.get(mapId);
    if (map == null) {
      maps.put(mapId, map = TmxMapParser.parse(mapId));
    }

    try {
      JSONObject newPlayerJson = new JSONObject();
      newPlayerJson.put("action", "overworld:login");
      newPlayerJson.put("spriteheight", 56);
      newPlayerJson.put("spritewidth", 42);
      newPlayerJson.put("name", player.id());
      newPlayerJson.put("image", player.getAvatar());
      newPlayerJson.put("x", player.getLocation().getLeft());
      newPlayerJson.put("y", player.getLocation().getTop());
      newPlayerJson.put("z", map.getEntityZ());
      newPlayerJson.put("map", mapId);
      PlayerManager.pushJson(player, newPlayerJson);

      newPlayerJson.put("action", "overworld:join");
      newPlayerJson.remove("map");

      JSONObject otherPlayerJson = new JSONObject();
      otherPlayerJson.put("action", "overworld:join");
      otherPlayerJson.put("spriteheight", 56);
      otherPlayerJson.put("spritewidth", 42);
      newPlayerJson.put("z", map.getEntityZ());

      for (String otherPlayerId : map.getPlayers()) {
        Player otherPlayer = PlayerManager.getPlayer(otherPlayerId);

        otherPlayerJson.put("name", otherPlayer.id());
        otherPlayerJson.put("image", otherPlayer.getAvatar());
        otherPlayerJson.put("x", otherPlayer.getLocation().getLeft());
        otherPlayerJson.put("y", otherPlayer.getLocation().getTop());

        PlayerManager.pushJson(player, otherPlayerJson);
        PlayerManager.pushJson(otherPlayer, newPlayerJson);
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
      else if ("look".equals(method)) {
        look(player, request);
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
    Location location = player.getLocation().translate(direction);

    if (location == null) {
      return;
    }

    Map map = maps.get(location.getMap());
    Entity entity = map.getEntity(location);

    if (entity != null) {
      if (entity.isSolid()) {
        return;
      }

      // TODO - handle regions
    }

    player.setLocation(location);

    JSONObject json = new JSONObject();
    json.put("action", "overworld:move");
    json.put("name", player.id());
    json.put("direction", direction);
    json.put("x", location.getLeft());
    json.put("y", location.getTop());

    for (String playerId : map.getPlayers()) {
      PlayerManager.pushJson(PlayerManager.getPlayer(playerId), json);
    }
  }

  public void look(Player player, JSONObject request) throws JSONException {
    String direction = request.getString("direction");

    if ("left".equals(direction) || "up".equals(direction) || "right".equals(direction) || "down".equals(direction)) {
      player.getLocation().setDirection(direction);
      JSONObject json = new JSONObject();
      json.put("action", "overworld:look");
      json.put("name", player.id());
      json.put("direction", direction);

      PlayerManager.pushJson(player, json);
    }
  }

  public void interact(Player player, JSONObject request) {
    Location location = player.getLocation().translate(player.getLocation().getDirection());
    Map map = maps.get(location.getMap());
    Entity entity = map.getEntity(location);

    if (entity == null) {
      return;
    }

    List<ActionSet> actionSets = entity.getActionSets("interact");

    // TODO - handle multiple action sets

    if (actionSets.size() > 0) {
      try {
        actionSets.get(0).execute(player);
      }
      catch (ServiceException e) {
        PlayerManager.pushMessage(player, new Message("error", e.getMessage()));
      }
    }
  }

  @Override
  public JSONObject load(JSONObject request, Player player) {
    return null;
  }
}