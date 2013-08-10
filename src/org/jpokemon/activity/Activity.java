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
   * @return If this Activity can be removed
   */
  public boolean onRemove(Player player);

  /**
   * Tells whether this Activity supports the indicated action. This hook is
   * provided for client-launched actions, and should be handled carefully.
   * 
   * @param action The action requested
   * @return If the activity permits this action to be executed
   */
  public boolean supportsAction(String action);

  public void handleRequest(Player player, JSONObject data) throws JSONException, ServiceException;
}