package org.jpokemon.overworld;

import java.util.HashMap;

import org.jpokemon.interaction.ActionSet;
import org.jpokemon.server.JPokemonService;
import org.jpokemon.server.Message;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldService implements JPokemonService {
  private HashMap<String, Map> maps = new HashMap<String, Map>();

  public Map getMap(String name) {
    if (!maps.containsKey(name)) {
      maps.put(name, TmxMapParser.parse(name));
    }

    return maps.get(name);
  }

  public void teleportPlayer(Player player, Location nextLocation) {
    sendLeave(player);
    player.setLocation(nextLocation);
    sendJoin(player);
  }

  @Override
  public void login(Player player) {
    sendJoin(player);
  }

  @Override
  public void logout(Player player) {
    sendLeave(player);
  }

  private void sendLeave(Player player) {
    String mapId = player.getLocation().getMap();
    Map map = getMap(mapId);
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

  private void sendJoin(Player player) {
    Location location = player.getLocation();
    String mapId = location.getMap();
    Map map = getMap(mapId);

    JSONObject sendToOthers = new JSONObject();
    JSONObject sendToPlayer = new JSONObject();
    JSONArray otherPlayersArray = new JSONArray();

    try {
      // Build the stuff to send to others
      sendToOthers.put("action", "overworld:join");
      sendToOthers.put("spriteheight", 56);
      sendToOthers.put("spritewidth", 42);
      sendToOthers.put("name", player.id());
      sendToOthers.put("image", player.getAvatar());
      sendToOthers.put("x", player.getLocation().getLeft());
      sendToOthers.put("y", player.getLocation().getTop());
      sendToOthers.put("z", map.getEntityZ());
      sendToOthers.put("direction", player.getLocation().getDirection());

      // build the stuff we already know about the player
      sendToPlayer.put("action", "overworld:load");
      sendToPlayer.put("spriteheight", 56);
      sendToPlayer.put("spritewidth", 42);
      sendToPlayer.put("name", player.id());
      sendToPlayer.put("image", player.getAvatar());
      sendToPlayer.put("x", player.getLocation().getLeft());
      sendToPlayer.put("y", player.getLocation().getTop());
      sendToPlayer.put("z", map.getEntityZ());
      sendToPlayer.put("direction", player.getLocation().getDirection());
      sendToPlayer.put("map", mapId);
      sendToPlayer.put("otherPlayers", otherPlayersArray);

      for (String playerId : map.getPlayers()) {
        Player otherPlayer = PlayerManager.getPlayer(playerId);
        PlayerManager.pushJson(otherPlayer, sendToOthers);
        JSONObject otherPlayerJson = new JSONObject();

        otherPlayerJson.put("name", otherPlayer.id());
        otherPlayerJson.put("spriteheight", 56);
        otherPlayerJson.put("spritewidth", 42);
        otherPlayerJson.put("image", otherPlayer.getAvatar());
        otherPlayerJson.put("x", otherPlayer.getLocation().getLeft());
        otherPlayerJson.put("y", otherPlayer.getLocation().getTop());
        otherPlayerJson.put("z", map.getEntityZ());
        otherPlayerJson.put("direction", otherPlayer.getLocation().getDirection());
        otherPlayersArray.put(otherPlayerJson);
      }

      map.addPlayer(player.id());
      PlayerManager.pushJson(player, sendToPlayer);
    }
    catch (JSONException e) { // this will never happen
      e.printStackTrace();
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

    Map map = getMap(location.getMap());
    Entity entity = map.getEntity(location);

    if (entity != null) {
      if (entity.isSolid()) {
        return;
      }

      for (ActionSet actionSet : entity.getActionSets("step")) {
        try {
          actionSet.execute(player);
        }
        catch (ServiceException e) {
          PlayerManager.pushMessage(player, new Message("error", e.getMessage()));
        }
      }
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
    Map map = getMap(player.getLocation().getMap());
    String direction = request.getString("direction");

    if ("left".equals(direction) || "up".equals(direction) || "right".equals(direction) || "down".equals(direction)) {
      player.getLocation().setDirection(direction);
      JSONObject json = new JSONObject();
      json.put("action", "overworld:look");
      json.put("name", player.id());
      json.put("direction", direction);

      for (String playerId : map.getPlayers()) {
        PlayerManager.pushJson(PlayerManager.getPlayer(playerId), json);
      }
    }
  }

  public void interact(Player player, JSONObject request) {
    Location location = player.getLocation().translate(player.getLocation().getDirection());
    Map map = getMap(location.getMap());
    Entity entity = map.getEntity(location);

    if (entity == null) {
      return;
    }

    for (ActionSet actionSet : entity.getActionSets("interact")) {
      try {
        actionSet.execute(player);
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
