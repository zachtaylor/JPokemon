package org.jpokemon.map.store;

import org.jpokemon.activity.ActivityService;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.json.JSONObject;

public class StoreService extends JPokemonService implements ActivityService {
  private StoreService() {
  }

  public static StoreService getInstance() {
    return instance;
  }

  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
  }

  @Override
  public void handleRequestOption(String option, JSONObject request) throws ServiceException {
    // Activity activity = getActivity(request);
    // Store store = ((StoreActivity) activity).getStore();

    // TODO : Do stuff to buy items
  }

  private static StoreService instance = new StoreService();
}