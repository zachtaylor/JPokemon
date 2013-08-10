package org.jpokemon.provider;

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

    try {
      data.put("action", "friends");
      data.put("friends", friendsList);

      for (String friend : player.getFriends()) {
        friendsList.put(friend);
      }
    } catch (JSONException e) {
    }

    return data;
  }
}