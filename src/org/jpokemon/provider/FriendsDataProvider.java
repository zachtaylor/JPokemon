package org.jpokemon.provider;

import org.jpokemon.action.FriendsAction;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsDataProvider {
  private FriendsDataProvider() {
  }

  public static JSONObject generate(Player player) {
    JSONObject data = new JSONObject();
    JSONArray friendsList = new JSONArray();
    JSONArray blockedList = new JSONArray();
    JSONArray pendingList = new JSONArray();

    try {
      data.put("action", "friends");
      data.put("friends", friendsList);
      data.put("blocked", blockedList);
      data.put("pending", pendingList);

      for (String friend : player.getFriends()) {
        friendsList.put(friend);
      }

      for (String blocked : player.getBlocked()) {
        blockedList.put(blocked);
      }

      for (String pending : FriendsAction.getPending(player.getName())) {
        pendingList.put(pending);
      }
    } catch (JSONException e) {
    }

    return data;
  }
}