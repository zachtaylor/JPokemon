package org.jpokemon.extra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.server.JPokemonService;
import org.jpokemon.server.Message;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsService implements JPokemonService {
  private static final Map<String, List<String>> pending = new HashMap<String, List<String>>();

  public void login(Player player) {
    pending.put(player.id(), new ArrayList<String>());
  }

  public void logout(Player player) {
    pending.remove(player.id());
  }

  @Override
  public void serve(JSONObject json, Player player) throws ServiceException {
    try {
      if (json.has("configure")) {
        configure(json, player);
        PlayerManager.pushJson(player, generateJson(player));
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }

  }

  @Override
  public JSONObject load(JSONObject request, Player player) {
    return generateJson(player);
  }

  private void configure(JSONObject json, Player player) throws JSONException {
    String configure = json.getString("configure");
    String otherPlayerName = json.getString("name");
    Player otherPlayer = PlayerManager.getPlayer(otherPlayerName);

    if (otherPlayer == null) { return; }
    if (otherPlayer.getBlocked().contains(player.id())) { return; }

    if (configure.equals("add")) {
      sendFriendRequest(player, otherPlayer);
    }
    else if (configure.equals("accept")) {
      acceptFriendRequest(player, otherPlayer);
    }
    else if (configure.equals("block")) {
      blockOtherPlayer(player, otherPlayer);
    }
  }

  private void sendFriendRequest(Player player, Player otherPlayer) {
    List<String> pendingList;
    synchronized (pending) {
      pendingList = pending.get(otherPlayer.id());
    }

    synchronized (pendingList) {
      pendingList.add(player.id());
    }

    PlayerManager.pushMessage(player, new Message("Friend request sent"));
    PlayerManager.pushMessage(otherPlayer, new Message("New friend request"));
    PlayerManager.pushJson(otherPlayer, generateJson(otherPlayer));
  }

  private void acceptFriendRequest(Player player, Player otherPlayer) {
    List<String> pendingList;
    synchronized (pending) {
      pendingList = pending.get(player.id());
    }

    synchronized (pendingList) {
      if (!pendingList.contains(otherPlayer.id())) { return; }

      player.addFriend(otherPlayer.id());
      otherPlayer.addFriend(player.id());

      pendingList.remove(otherPlayer.id());
    }

    Message message = new Message("Friend request accepted");
    PlayerManager.pushMessage(player, message);
    PlayerManager.pushMessage(otherPlayer, message);
    PlayerManager.pushJson(otherPlayer, generateJson(otherPlayer));
  }

  private void blockOtherPlayer(Player player, Player otherPlayer) {
    player.addBlocked(otherPlayer.id());
    otherPlayer.removeFriend(player.id());

    PlayerManager.pushMessage(player, new Message("Player blocked"));
  }

  private JSONObject generateJson(Player player) {
    JSONObject json = new JSONObject();

    try {
      json.put("action", "friends");
      json.put("friends", new JSONArray(player.getFriends()));
      json.put("blocked", new JSONArray(player.getBlocked()));

      List<String> pendingList;
      synchronized (pending) {
        pendingList = pending.get(player.id());
      }
      synchronized (pendingList) {
        json.put("pending", new JSONArray(pendingList));
      }
    }
    catch (JSONException e) {
    }

    return json;
  }
}