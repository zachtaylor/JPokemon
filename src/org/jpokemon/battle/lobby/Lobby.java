package org.jpokemon.battle.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
  private static final Map<String, Lobby> lobbies = new HashMap<String, Lobby>();

  private final String host;
  private boolean open = false;
  private final List<List<String>> teams = new ArrayList<List<String>>();
  private final Map<String, String> responses = new HashMap<String, String>();

  private Lobby(String h) {
    host = h;
    reset();
  }

  public static Lobby get(String playerId) {
    Lobby lobby;

    synchronized (lobbies) {
      lobby = lobbies.get(playerId);

      if (lobby == null) {
        lobby = new Lobby(playerId);
        lobbies.put(playerId, lobby);
      }
    }

    return lobby;
  }

  public static void clear(String playerId) {
    synchronized (lobbies) {
      lobbies.remove(playerId);
    }
  }

  public String getHost() {
    return host;
  }

  public boolean isOpen() {
    return open;
  }

  public void setOpen(boolean state) {
    open = state;

    if (open) {
      responses.clear();

      for (List<String> team : teams) {
        for (String name : team) {
          if (name.equals(host)) {
            responses.put(name, "yes");
          }
          else {
            responses.put(name, "pending");
          }
        }
      }
    }
  }

  public void addTeam() {
    teams.add(new ArrayList<String>());
  }

  public void removeTeam(int team) {
    if (!open) {
      teams.remove(team);
    }
  }

  public void addPlayer(String otherPlayerName, int team) {
    teams.get(team).add(otherPlayerName);
  }

  public void accept(String otherPlayerName) {
    responses.put(otherPlayerName, "yes");
  }

  public void reject(String otherPlayerName) {
    responses.put(otherPlayerName, "no");
  }

  public List<List<String>> getTeams() {
    List<List<String>> copy = new ArrayList<List<String>>();

    for (List<String> team : teams) {
      copy.add(Collections.unmodifiableList(team));
    }

    return Collections.unmodifiableList(copy);
  }

  public Map<String, String> getResponses() {
    return Collections.unmodifiableMap(responses);
  }

  public void reset() {
    teams.clear();
    teams.add(new ArrayList<String>());
    teams.add(new ArrayList<String>());
    teams.get(0).add(host);
    responses.clear();
  }
}