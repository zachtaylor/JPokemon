package org.jpokemon.extra;

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

public class FriendsService implements JPokemonService {
  private static final Map<String, List<String>> pending = new HashMap<String, List<String>>();

  @Override
  public void serve(JSONObject json, Player player) throws ServiceException {
    try {
      if (json.has("configure")) {
        configure(json, player);
        PlayerManager.pushJson(player, generateJson(player));
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

  }

  private void configure(JSONObject json, Player player) throws JSONException {
    String configure = json.getString("configure");
    String otherPlayerName = json.getString("name");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    if (otherPlayer == null) { return; }
    if (otherPlayer.getBlocked().contains(player.id())) { return; }

    if (configure.equals("add")) {
      List<String> pendingList;
      synchronized (pending) {
        if (pending.get(otherPlayerName) == null) {
          pending.put(otherPlayerName, new ArrayList<String>());
        }
        pendingList = pending.get(otherPlayerName);
      }

      synchronized (pendingList) {
        pendingList.add(player.id());
      }

      PlayerManager.pushMessage(player, new Message.Notification("Friend request sent"));
      PlayerManager.pushMessage(otherPlayer, new Message.Notification("New friend request"));
      PlayerManager.pushJson(otherPlayer, generateJson(otherPlayer));
    }
    else if (configure.equals("accept")) {
      List<String> pendingList;
      synchronized (pending) {
        pendingList = pending.get(player.id());
      }

      if (pendingList == null) { return; }

      synchronized (pendingList) {
        if (!pendingList.contains(otherPlayerName)) { return; }

        player.addFriend(otherPlayerName);
        otherPlayer.addFriend(player.getName());

        pendingList.remove(otherPlayerName);
      }

      PlayerManager.pushMessage(player, new Message.Notification("Friend request accepted"));
      PlayerManager.pushJson(player, generateJson(player));
      PlayerManager.pushMessage(otherPlayer, new Message.Notification("Friend request accepted"));
      PlayerManager.pushJson(otherPlayer, generateJson(otherPlayer));
    }
    else if (configure.equals("block")) {
      player.addBlocked(otherPlayerName);
      otherPlayer.removeFriend(player.getName());

      PlayerManager.pushMessage(player, new Message.Notification("Player blocked"));
    }
  }

  @Override
  public JSONObject load(JSONObject request, Player player) {
    return generateJson(player);
  }

  private JSONObject generateJson(Player player) {
    JSONObject json = new JSONObject();
    JSONArray friendsArray = new JSONArray();
    JSONArray blockedArray = new JSONArray();
    JSONArray pendingArray = new JSONArray();

    try {
      json.put("action", "friends");

      for (String friend : player.getFriends()) {
        friendsArray.put(friend);
      }
      json.put("friends", friendsArray);

      for (String blocked : player.getBlocked()) {
        blockedArray.put(blocked);
      }
      json.put("blocked", blockedArray);

      List<String> pendingList;
      synchronized (pending) {
        pendingList = pending.get(player.id());
      }
      if (pendingList == null) {
        pendingList = new ArrayList<String>();
      }

      synchronized (pendingList) {
        for (String pending : pendingList) {
          pendingArray.put(pending);
        }
      }
      json.put("pending", pendingArray);
    } catch (JSONException e) {
    }

    return json;
  }
}