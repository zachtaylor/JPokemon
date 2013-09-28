package org.jpokemon.battle.activity;

import org.jpokemon.activity.Activity;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectTargetForMoveActivity implements Activity {
  /** Name of the move being used */
  private String moveName;

  /** Target id specified by the user */
  private String targetId;

  public SelectTargetForMoveActivity(String name) {
    moveName = name;
  }

  public String getTargetId() {
    return targetId;
  }

  @Override
  public void onAdd(Player player) throws ServiceException {
    JSONObject json = new JSONObject();

    try {
      json.put("action", "battle:selecttarget");
      json.put("moveName", moveName);
    }
    catch (JSONException e) {
    }

    PlayerManager.pushJson(player, json);
  }

  @Override
  public void logout(Player player) {
  }

  @Override
  public void onReturn(Activity activity, Player player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    try {
      if (request.has("targetId")) {
        targetId = request.getString("targetId");

        PlayerManager.popActivity(player, this);

        return;
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }

    throw new ServiceException("targetId expected");
  }
}