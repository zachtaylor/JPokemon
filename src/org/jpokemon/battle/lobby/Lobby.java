package org.jpokemon.battle.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.trainer.Player;

public class Lobby {
  private static final Map<String, Lobby> lobbies = new HashMap<String, Lobby>();
  private static final Map<String, String> pending = new HashMap<String, String>();

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
    synchronized (this) {
      if (!open) {
        teams.get(team).add(otherPlayerName);
      }
    }
  }

  public void setOpen(boolean state) {
    synchronized (this) {
      open = state;

      if (open) {
        responses.clear();

        for (List<String> team : teams) {
          for (String name : team) {
            responses.put(name, "none");
          }
        }
      }
    }
  }

  public void accept(String otherPlayerName) {
    synchronized (this) {
      responses.put(otherPlayerName, "yes");
    }
  }

  public void reject(String otherPlayerName) {
    synchronized (this) {
      responses.put(otherPlayerName, "no");
    }
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
}