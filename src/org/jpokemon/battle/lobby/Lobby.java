package org.jpokemon.battle.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.server.Message;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Lobby {
  private static final Map<String, Lobby> lobbies = new HashMap<String, Lobby>();
  private static final Map<String, List<String>> pending = new HashMap<String, List<String>>();

  private final String host;
  private boolean open = false;
  private final List<List<String>> teams = new ArrayList<List<String>>();
  private final Map<String, String> responses = new HashMap<String, String>();

  private Lobby(String h) {
    host = h;
    reset();
  }

  public static Lobby get(Player player) {
    Lobby lobby;

    synchronized (lobbies) {
      lobby = lobbies.get(player.id());

      if (lobby == null) {
        lobby = new Lobby(player.id());
        lobbies.put(player.id(), lobby);
      }
    }

    return lobby;
  }

  public void addTeam() {
    synchronized (this) {
      if (!open) {
        teams.add(new ArrayList<String>());
      }
    }
  }

  public void addPlayer(String otherPlayerName, int team) {
    Player player = PlayerManager.getPlayer(otherPlayerName);

    if (player == null) {
      Message message = new Message.Notification("'" + otherPlayerName + "' not found");
      PlayerManager.pushMessage(PlayerManager.getPlayer(host), message);
    }

    synchronized (this) {
      if (!open) {
        teams.get(team).add(otherPlayerName);
      }
    }
  }

  public void setOpen(boolean state) {
    Message message = new Message.Notification("New battle request");

    synchronized (this) {
      open = state;

      if (open) {
        responses.clear();

        for (List<String> team : teams) {
          for (String name : team) {
            if (name.equals(host)) {
              responses.put(name, "yes");
            }
            else {
              Player player = PlayerManager.getPlayer(name);
              PlayerManager.pushMessage(player, message);
              addToPending(host, name);
              responses.put(name, "none");
            }
          }
        }
      }
    }

    message = new Message.Notification("Requests sent");
    PlayerManager.pushMessage(PlayerManager.getPlayer(host), message);
  }

  public void accept(String otherPlayerName) {
    synchronized (this) {
      responses.put(otherPlayerName, "yes");
      removeFromPending(host, otherPlayerName);
    }
  }

  public void reject(String otherPlayerName) {
    synchronized (this) {
      responses.put(otherPlayerName, "no");
      removeFromPending(host, otherPlayerName);
    }
  }

  public void start() {

  }

  public void reset() {
    synchronized (this) {
      teams.clear();
      teams.add(new ArrayList<String>());
      teams.add(new ArrayList<String>());
      teams.get(0).add(host);
      responses.clear();
    }
  }

  public static JSONObject generateJson(Player player) {
    Lobby lobby = get(player);
    JSONObject json = new JSONObject();

    try {
      json.put("action", "lobby");
      json.put("open", lobby.open);
      json.put("teams", new JSONArray(lobby.teams.toString())); // sneaky
    }
    catch (JSONException e) {
    }

    return json;
  }

  private static void addToPending(String from, String to) {
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

  private static void removeFromPending(String from, String to) {
    List<String> pendingList;

    synchronized (pending) {
      pendingList = pending.get(to);
    }

    if (pendingList == null) { return; }

    synchronized (pendingList) {
      pendingList.remove(from);
    }
  }
}