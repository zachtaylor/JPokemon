package org.jpokemon.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsDataProvider {
  private static final Map<String, List<String>> pending = new HashMap<String, List<String>>();

  private FriendsDataProvider() {
  }

  public static void addPendingRequest(String from, String to) {
    List<String> pendingList;

    synchronized (pending) {
      if (pending.get(to) == null) {
        pending.put(to, new ArrayList<String>());
      }

      pendingList = pending.get(to);
    }

    synchronized (pendingList) {
      pendingList.add(from);
    }
  }

  public static boolean hasPendingRequest(String from, String to) {
    List<String> pendingList;

    synchronized (pending) {
      pendingList = pending.get(to);
    }

    boolean result = false;

    if (pendingList != null) {
      synchronized (pendingList) {
        result = pendingList.contains(from);
      }
    }

    return result;
  }

  public static void removePendingRequest(String from, String to) {
    List<String> pendingList;

    synchronized (pending) {
      pendingList = pending.get(to);
    }

    if (pendingList == null) { return; }

    synchronized (pendingList) {
      pendingList.remove(from);
    }
  }

  public static JSONObject generate(Player player) {
    JSONObject data = new JSONObject();
    JSONArray friendsArray = new JSONArray();
    JSONArray blockedArray = new JSONArray();
    JSONArray pendingArray = new JSONArray();

    try {
      data.put("action", "friends");
      data.put("friends", friendsArray);
      data.put("blocked", blockedArray);
      data.put("pending", pendingArray);

      for (String friend : player.getFriends()) {
        friendsArray.put(friend);
      }

      for (String blocked : player.getBlocked()) {
        blockedArray.put(blocked);
      }

      List<String> pendingList;
      synchronized (pending) {
        pendingList = pending.get(player.id());
      }

      if (pendingList != null) {
        synchronized (pendingList) {
          for (String pending : pendingList) {
            pendingArray.put(pending);
          }
        }
      }
    } catch (JSONException e) {
    }

    return data;
  }
}