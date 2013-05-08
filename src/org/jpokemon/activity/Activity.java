package org.jpokemon.activity;

import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public interface Activity {
  public String getName();

  public void handleRequest(JSONObject request) throws ServiceException;
  
  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException;
}