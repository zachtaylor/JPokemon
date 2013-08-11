package org.jpokemon.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsAction extends Action {
  private static final Map<String, List<String>> pending = new HashMap<String, List<String>>();

  public static List<String> getPending(String player) {
    if (pending.get(player) == null) { return new ArrayList<String>(); }

    return Collections.unmodifiableList(pending.get(player));
  }

  public FriendsAction(JSONObject json) {
    data = json;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    try {
      if (data.has("add")) {
        addFriend(player);
      }
      else if (data.has("accept")) {
        acceptFriend(player);
      }
      else if (data.has("block")) {
        blockFriend(player);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    PlayerManager.pushJson(player, "friends");
  }

  private void addFriend(Player player) throws JSONException {
    String otherPlayerName = data.getString("add");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    if (otherPlayer.getBlocked().contains(player.getName())) { return; }
    if (pending.get(otherPlayerName) == null) {
      pending.put(otherPlayerName, new ArrayList<String>());
    }

    pending.get(otherPlayerName).add(player.getName());
  }

  private void acceptFriend(Player player) throws JSONException {
    String otherPlayerName = data.getString("accept");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    if (pending.get(player.getName()) == null) { return; }
    if (!pending.get(player.getName()).contains(otherPlayerName)) { return; }

    player.addFriend(otherPlayerName);
    otherPlayer.addFriend(player.getName());

    pending.get(player.getName()).remove(otherPlayerName);
  }

  private void blockFriend(Player player) throws JSONException {
    String otherPlayerName = data.getString("block");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    player.addBlocked(otherPlayerName);
    otherPlayer.removeFriend(player.getName());
  }

  private JSONObject data;
}