package org.jpokemon.extra;

import org.jpokemon.activity.Activity;
import org.jpokemon.overworld.OverworldService;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DresserActivity implements Activity {
  @Override
  public void onAdd(Player player) throws ServiceException {
    JSONObject json = new JSONObject();
    JSONArray avatars = new JSONArray();

    for (String avatar : player.getAvatars()) {
      avatars.put(avatar);
    }

    try {
      json.put("action", "dresser.Dresser:open");
      json.put("avatar", player.getAvatar());
      json.put("avatars", avatars);
    }
    catch (JSONException e) {
    }

    PlayerManager.pushJson(player, json);
  }

  @Override
  public void logout(Player player) { // Do nothing
  }

  @Override
  public void onReturn(Activity activity, Player player) { // Do nothing
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    try {
      String nextAvatar = request.getString("avatar");

      if (player.getAvatar().equals(nextAvatar)) {
        return;
      }
      if (!player.getAvatars().contains(nextAvatar)) {
        return;
      }

      player.setAvatar(nextAvatar);
      PlayerManager.popActivity(player, this);

      JSONObject json = new JSONObject();
      json.put("action", "dresser.Dresser:close");
      PlayerManager.pushJson(player, json);

      OverworldService overworldService = (OverworldService) PlayerManager.getService("overworld");

      // This is sort-of a refresh all
      overworldService.teleportPlayer(player, player.getLocation());
    }
    catch (JSONException e) {
    }
  }
}
