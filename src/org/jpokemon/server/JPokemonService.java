package org.jpokemon.server;

import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public interface JPokemonService {
  public void login(Player player);

  public void logout(Player player);

  public void serve(JSONObject request, Player player) throws ServiceException;

  public JSONObject load(JSONObject request, Player player);
}