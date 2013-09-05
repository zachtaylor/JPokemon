package org.jpokemon.action;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.battle.lobby.Lobby;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class LobbyAction extends Action {
  private JSONObject json;

  public LobbyAction(JSONObject j) {
    json = j;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    try {
      if (json.has("configure")) {
        configure(player);
      }

      PlayerManager.pushJson(player, Lobby.generateJson(player));
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void configure(Player player) throws JSONException {
    if ("addteam".equals(json.getString("configure"))) {
      Lobby.get(player).addTeam();
    }
    else if ("addplayer".equals(json.getString("configure"))) {
      String otherPlayerName = json.getString("name");
      int team = json.getInt("team");
      Lobby.get(player).addPlayer(otherPlayerName, team);
    }
    else if ("openstate".equals(json.getString("configure"))) {
      boolean open = json.getBoolean("openstate");
      Lobby.get(player).setOpen(open);
    }
  }
}