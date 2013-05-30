package org.jpokemon.manager;

import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JPokemonService {
  private static final String PLAYER_ID_KEY = "id", OPTION_KEY = "option";

  protected static Player getPlayer(JSONObject request) throws ServiceException {
    String playerID = null;
    Player player = null;

    try {
      playerID = request.getString(PLAYER_ID_KEY);
    } catch (JSONException e) {
      throw new ServiceException("Player key not found");
    }

    player = PlayerManager.getPlayer(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    return player;
  }

  protected static Activity getActivity(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);

    return PlayerManager.getActivity(player);
  }

  protected static String getOption(JSONObject request) throws ServiceException {
    String option = null;

    try {
      option = request.getString(OPTION_KEY);
    } catch (JSONException e) {
      throw new ServiceException("Option not found");
    }

    return option;
  }

  public abstract void handleRequest(JSONObject request) throws ServiceException;

  public abstract void handleRequestOption(String option, JSONObject request) throws ServiceException;
}