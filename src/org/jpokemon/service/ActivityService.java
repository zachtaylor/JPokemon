package org.jpokemon.service;

import org.jpokemon.activity.Activity;
import org.json.JSONObject;

public class ActivityService extends JPokemonService {
  public static void submit(JSONObject request) throws ServiceException {
    Activity activity = getActivity(request);

    activity.handleRequest(request);
  }
}