package org.jpokemon.activity;

import org.jpokemon.service.ServiceException;
import org.json.JSONObject;

public interface ActivityService {
  public void handleRequest(JSONObject request) throws ServiceException;

  public void handleRequestOption(String option, JSONObject request) throws ServiceException;
}