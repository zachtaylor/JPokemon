package org.jpokemon.battle.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.server.JPokemonService;
import org.jpokemon.server.Message;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LobbyService implements JPokemonService {
  private static final Map<String, List<String>> pending = new HashMap<String, List<String>>();

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    try {
      if (request.has("configure")) {
        configure(request, player);
      }
      else if (request.has("respond")) {

      }

      PlayerManager.pushJson(player, generateJson(player.id()));
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void configure(JSONObject json, Player player) throws JSONException {
    if ("addteam".equals(json.getString("configure"))) {
      Lobby.get(player.id()).addTeam();
    }
    else if ("addplayer".equals(json.getString("configure"))) {
      String otherPlayerName = json.getString("name");
      int team = json.getInt("team");
      Lobby.get(player.id()).addPlayer(otherPlayerName, team);
    }
    else if ("openstate".equals(json.getString("configure"))) {
      boolean open = json.getBoolean("openstate");
      Lobby lobby = Lobby.get(player.id());

      lobby.setOpen(open);

      if (open) {
        Message message = new Message.Notification("New battle request");
        JSONObject lobbyJson = generateJson(player.id());

        lobbyJson.put("action", "lobbypending");

        for (List<String> team : lobby.getTeams()) {
          for (String name : team) {
            if (name.equals(lobby.getHost())) {
              continue;
            }
            else {
              Player p = PlayerManager.getPlayer(name);
              PlayerManager.pushMessage(p, message);
              PlayerManager.pushJson(p, lobbyJson);
              LobbyService.addToPending(lobby.getHost(), name);
            }
          }
        }

        message = new Message.Notification("Requests sent");
        PlayerManager.pushMessage(player, message);
      }
    }
  }

  @Override
  public JSONObject load(JSONObject request, Player player) throws ServiceException {
    String host = player.id();

    if (request.has("host")) {
      try {
        host = request.getString("host");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    JSONObject json = generateJson(host);

    if (request.has("host")) {
      try {
        json.put("action", "lobbypending");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }

    return json;
  }

  public static void addToPending(String from, String to) {
    List<String> pendingList;

    synchronized (pending) {
      pendingList = pending.get(to);

      if (pendingList == null) {
        pendingList = new ArrayList<String>();
        pending.put(to, pendingList);
      }
    }

    synchronized (pendingList) {
      pendingList.add(from);
    }
  }

  public static void removeFromPending(String from, String to) {
    List<String> pendingList;

    synchronized (pending) {
      pendingList = pending.get(to);
    }

    if (pendingList == null) { return; }

    synchronized (pendingList) {
      pendingList.remove(from);
    }
  }

  private JSONObject generateJson(String playerId) {
    Lobby lobby = Lobby.get(playerId);
    JSONObject json = new JSONObject();

    try {
      json.put("action", "lobby");
      json.put("host", lobby.getHost());
      json.put("open", lobby.isOpen());
      json.put("teams", new JSONArray(lobby.getTeams().toString())); // sneaky

      if (lobby.isOpen()) {
        json.put("responses", new JSONObject(lobby.getResponses()));
      }

      List<String> pendingList;

      synchronized (pending) {
        pendingList = pending.get(playerId);
      }

      if (pendingList != null) {
        json.put("pending", new JSONArray(pendingList.toString()));
      }
      else {
        json.put("pending", new JSONArray());
      }
    } catch (JSONException e) {
    }

    return json;
  }
}