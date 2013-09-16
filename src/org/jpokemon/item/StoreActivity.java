package org.jpokemon.item;

import org.jpokemon.activity.Activity;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreActivity implements Activity {
  private Store store;

  public StoreActivity(Store s) {
    store = s;
  }

  @Override
  public void onAdd(Player player) {
  }

  @Override
  public void onReturn(Activity activity, Player player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    try {
      JSONArray itemChanges = request.getJSONArray("items");

      int runningTotalCash = player.getCash();

      for (int index = 0; index < itemChanges.length(); index++) {
        JSONObject itemChange = itemChanges.getJSONObject(index);

        int itemID = itemChange.getInt("item");
        int change = itemChange.getInt("change");
        int denomination = itemChange.getInt("denomination");

        Inventory inventory = findInventory(itemID, denomination);

        if (inventory == null) { throw new ServiceException("Invalid sale request item:" + itemID + " denomination:" + denomination); }

        if (change > 0) {
          runningTotalCash -= change * inventory.getPrice();
        }
        else {
          runningTotalCash += change * inventory.getPurchaseprice();

          // The player does not have enough of that item to sell
          if (player.item(itemID).amount() >= -change) { throw new ServiceException("Insufficent quantity item : " + player.item(itemID).name()); }
        }
      }

      if (!(runningTotalCash >= 0)) { throw new ServiceException("Not enough cash to complete order"); }

      for (int index = 0; index < itemChanges.length(); index++) {
        JSONObject itemChange = itemChanges.getJSONObject(index);

        int itemID = itemChange.getInt("item");
        int change = itemChange.getInt("change");
        int denomination = itemChange.getInt("denomination");

        Inventory inventory = findInventory(itemID, denomination);

        player.item(itemID).add(change);

        if (change > 0) {
          player.setCash(player.getCash() - change * inventory.getPrice());
        }
        else {
          // Change is negative. Double negative causes addition of money
          player.setCash(player.getCash() - change * inventory.getPurchaseprice());
        }
      }
    }
    catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private Inventory findInventory(int itemID, int denomination) {
    for (Inventory inventory : store) {
      if (inventory.getItem() == itemID && inventory.getDenomination() == denomination) { return inventory; }
    }

    return null;
  }
}