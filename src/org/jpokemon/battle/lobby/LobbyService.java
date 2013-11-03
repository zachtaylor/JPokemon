package org.jpokemon.battle.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.battle.Battle;
import org.jpokemon.server.JPokemonService;
import org.jpokemon.server.Message;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LobbyService implements JPokemonService {
  private static final Map<String, Lobby> lobbies = new HashMap<String, Lobby>();
  private static final Map<String, List<String>> pending = new HashMap<String, List<String>>();

  public void login(Player player) {
    lobbies.put(player.id(), new Lobby(player.id()));
    pending.put(player.id(), new ArrayList<String>());
  }

  public void logout(Player player) {
    lobbies.remove(player.id());
    pending.remove(player.id());
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    try {
      if (request.has("configure")) {
        configure(request, player);
      }
      else if (request.has("respond")) {
        respond(request, player);
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public JSONObject load(JSONObject request, Player player) {
    String host = player.id();

    if (request.has("host")) {
      try {
        host = request.getString("host");
      }
      catch (JSONException e) {
        e.printStackTrace();
      }
    }

    Lobby lobby = lobbies.get(host);
    JSONObject json = generateJson(lobby);

    List<String> pendingList;
    synchronized (pending) {
      pendingList = pending.get(player.id());
    }

    try {
      json.put("pending", new JSONArray(pendingList.toString()));
    }
    catch (JSONException e) {
      e.printStackTrace();
    }

    return json;
  }

  private void configure(JSONObject json, Player player) throws JSONException, ServiceException {
    String configure = json.getString("configure");
    Lobby lobby = lobbies.get(player.id());

    if (configure.equals("addteam")) {
      if (!lobby.isConfiguring()) { return; }

      lobby.addTeam();
      PlayerManager.pushJson(player, load(new JSONObject(), player));
    }
    else if (configure.equals("addplayer")) {
      String otherPlayerName = json.getString("name");
      int team = json.getInt("team");

      if (!lobby.isConfiguring()) { return; }

      if (!PlayerManager.playerIsLoggedIn(otherPlayerName)) {
        Message message = new Message("'" + otherPlayerName + "' not found");
        PlayerManager.pushMessage(player, message);
        return;
      }

      lobby.addPlayer(otherPlayerName, team);
      PlayerManager.pushJson(player, load(new JSONObject(), player));
    }
    else if (configure.equals("removeplayer")) {
      String otherPlayerName = json.getString("name");
      int team = json.getInt("team");

      if (!lobby.isConfiguring()) { return; }

      lobby.removePlayer(otherPlayerName, team);
      PlayerManager.pushJson(player, load(new JSONObject(), player));
    }
    else if (configure.equals("state")) {
      String state = json.getString("state");

      if ("configure".equals(state)) {
        clearPending(lobby);
        lobby.setConfiguring(true);
        PlayerManager.pushJson(player, load(new JSONObject(), player));
      }
      else if ("wait".equals(state)) {
        buildPending(lobby);
        lobby.setConfiguring(false);
        pushLobbyToPlayers(lobby, true);
      }
      else if ("start".equals(state)) {
        clearPending(lobby);
        startBattle(lobby);
        lobby.reset();
      }
    }
  }

  private void respond(JSONObject json, Player player) throws JSONException, ServiceException {
    String host = json.getString("host");
    String response = json.getString("respond");

    Lobby lobby = lobbies.get(host);

    if (lobby.isConfiguring() || !lobby.getResponses().keySet().contains(player.id())) { return; }

    if (response.equals("accept")) {
      lobby.accept(player.id());
    }
    else if (response.equals("reject")) {
      lobby.reject(player.id());
    }

    pushLobbyToPlayers(lobby, false);
  }

  private JSONObject generateJson(Lobby lobby) {
    JSONObject json = new JSONObject();

    try {
      json.put("action", "lobby");
      json.put("host", lobby.getHost());
      json.put("state", lobby.isConfiguring() ? "configure" : "wait");
      json.put("teams", new JSONArray(lobby.getTeams().toString()));
      json.put("responses", new JSONObject(lobby.getResponses()));
    }
    catch (JSONException e) {
    }

    return json;
  }

  private void buildPending(Lobby lobby) {
    for (List<String> team : lobby.getTeams()) {
      for (String name : team) {
        if (name.equals(lobby.getHost())) {
          continue;
        }

        List<String> pendingList;
        synchronized (pending) {
          pendingList = pending.get(name);
        }
        synchronized (pendingList) {
          pendingList.add(lobby.getHost());
        }

        Player player = PlayerManager.getPlayer(name);
        PlayerManager.pushJson(player, load(new JSONObject(), player));
      }
    }
  }

  private void clearPending(Lobby lobby) {
    for (List<String> team : lobby.getTeams()) {
      for (String name : team) {
        if (name.equals(lobby.getHost())) {
          continue;
        }

        List<String> pendingList;
        synchronized (pending) {
          pendingList = pending.get(name);
        }
        synchronized (pendingList) {
          pendingList.remove(lobby.getHost());
        }

        Player player = PlayerManager.getPlayer(name);
        PlayerManager.pushJson(player, load(new JSONObject(), player));
      }
    }
  }

  private void startBattle(Lobby lobby) {
    Battle battle = new Battle();
    Map<String, String> responses = lobby.getResponses();
    List<Player> playersInBattle = new ArrayList<Player>();

    int teamNumber = 0;
    for (List<String> teamMembers : lobby.getTeams()) {
      for (String teamMember : teamMembers) {
        if ("yes".equals(responses.get(teamMember))) {
          Player player = PlayerManager.getPlayer(teamMember);
          battle.addTrainer(player, teamNumber);
          playersInBattle.add(player);
        }
      }

      teamNumber++;
    }

    for (Player player : playersInBattle) {
      PlayerManager.addActivity(player, battle);
    }
  }

  private void pushLobbyToPlayers(Lobby lobby, boolean sendNewRequestMessage) {
    Message message = new Message("New battle request");
    JSONObject lobbyJson = generateJson(lobby);

    for (List<String> team : lobby.getTeams()) {
      for (String name : team) {
        Player player = PlayerManager.getPlayer(name);
        PlayerManager.pushJson(player, lobbyJson);

        if (sendNewRequestMessage) {
          PlayerManager.pushMessage(player, message);
        }
      }
    }
  }
}