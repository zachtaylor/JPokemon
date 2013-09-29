package org.jpokemon.battle.lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
  private final String host;
  private boolean configuring = true;
  private final List<List<String>> teams = new ArrayList<List<String>>();
  private final Map<String, String> responses = new HashMap<String, String>();

  public Lobby(String h) {
    host = h;
    reset();
  }

  public String getHost() {
    return host;
  }

  public boolean isConfiguring() {
    return configuring;
  }

  public void setConfiguring(boolean state) {
    configuring = state;

    if (!configuring) {
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
    teams.remove(team);
  }

  public void addPlayer(String otherPlayerName, int team) {
    teams.get(team).add(otherPlayerName);
  }

  public void removePlayer(String otherPlayerName, int team) {
    teams.get(team).remove(otherPlayerName);
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
    configuring = true;
  }
}