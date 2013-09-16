package org.jpokemon.activity;

import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public interface Activity {
  public void onAdd(Player player) throws ServiceException;

  public void onReturn(Activity activity, Player player);

  public void serve(JSONObject request, Player player) throws ServiceException;
}