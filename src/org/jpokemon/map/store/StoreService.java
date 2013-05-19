package org.jpokemon.map.store;

import org.jpokemon.activity.ActivityService;
import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreService extends JPokemonService implements ActivityService {
  private StoreService() {
  }

  public static StoreService getInstance() {
    return instance;
  }

  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Store store = ((StoreActivity) getActivity(request)).getStore();

    try {
      JSONArray itemChanges = request.getJSONArray("items");

      int runningTotalCash = player.cash();

      for (int index = 0; index < itemChanges.length(); index++) {
        JSONObject itemChange = itemChanges.getJSONObject(index);

        int itemID = itemChange.getInt("item");
        int change = itemChange.getInt("change");
        int denomination = itemChange.getInt("denomination");

        Inventory inventory = findInventory(store, itemID, denomination);

        if (inventory == null) {
          throw new ServiceException("Invalid sale request item:" + itemID + " denomination:" + denomination);
        }

        if (change > 0) {
          runningTotalCash -= change * inventory.getPrice();
        }
        else {
          runningTotalCash += change * inventory.getPurchaseprice();

          // The player does not have enough of that item to sell
          if (player.item(itemID).amount() >= -change) {
            throw new ServiceException("Insufficent quantity item : " + player.item(itemID).name());
          }
        }
      }

      if (!(runningTotalCash >= 0)) {
        throw new ServiceException("Not enough cash to complete order");
      }

      for (int index = 0; index < itemChanges.length(); index++) {
        JSONObject itemChange = itemChanges.getJSONObject(index);

        int itemID = itemChange.getInt("item");
        int change = itemChange.getInt("change");
        int denomination = itemChange.getInt("denomination");

        Inventory inventory = findInventory(store, itemID, denomination);

        player.item(itemID).add(change);

        if (change > 0) {
          player.cash(player.cash() - change * inventory.getPrice());
        }
        else {
          // Change is negative. Double negative causes addition of money
          player.cash(player.cash() - change * inventory.getPurchaseprice());
        }
      }

      ActivityTracker.clearActivity(player);
    } catch (JSONException e) {
    }
  }

  @Override
  public void handleRequestOption(String option, JSONObject request) throws ServiceException {
    throw new ServiceException("Stores have no options");
  }

  private Inventory findInventory(Store store, int itemID, int denomination) {
    for (Inventory inventory : store) {
      if (inventory.getItem() == itemID && inventory.getDenomination() == denomination) {
        return inventory;
      }
    }

    return null;
  }

  private static StoreService instance = new StoreService();
}