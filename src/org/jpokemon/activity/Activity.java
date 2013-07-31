package org.jpokemon.activity;

import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public interface Activity {
  /**
   * Called when this activity is added to the player. If this Activity should
   * fail, for whatever reason, "false" return will terminate the addition.
   * 
   * @param player The player this activity is being added to
   * @return If the activity should be added to the player's activity stack
   */
  public boolean onAdd(Player player);

  /**
   * Called when this activity is removed from a player. If this Activity cannot
   * be removed, for whatever reason, "false" return will terminate the removal.
   * 
   * @param player
   * @return
   */
  public boolean onRemove(Player player);

  public void handleRequest(Player player, JSONObject data) throws JSONException, ServiceException;
}