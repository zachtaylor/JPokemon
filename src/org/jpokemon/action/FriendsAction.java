package org.jpokemon.action;

import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendsAction extends Action {
  public FriendsAction(JSONObject json) {
    data = json;
  }

  @Override
  public void execute(Player player) throws ServiceException {
    try {
      player.addFriend(data.getString("add"));
    } catch (JSONException e) {
      e.printStackTrace();
    }

    PlayerManager.pushJson(player, "friends");
  }

  private JSONObject data;
}