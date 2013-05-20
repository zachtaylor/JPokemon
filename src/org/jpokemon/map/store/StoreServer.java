package org.jpokemon.map.store;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.ActivityServer;
import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.item.Item;
import org.jpokemon.map.Map;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreServer extends ActivityServer {
  public StoreServer(Player player) {
    super(player);

    visit(player);
  }

  public void visit(Player player) {
    Activity activity = ActivityTracker.getActivity(player);

    Store store = ((StoreActivity) activity).getStore();

    try {
      data().put("area_name", Map.area(player.area()).getName());
      data().put("cash", player.cash());
      data().put("inventory", inventoryItems = new JSONArray());
    } catch (JSONException e) {
    }

    for (Inventory inventory : store) {
      visit(inventory);
    }
  }

  public void visit(Inventory inventory) {
    inventoryItem = new JSONObject();

    try {
      inventoryItem.put("id", inventory.getItem());
      inventoryItem.put("price", inventory.getPrice());
      inventoryItem.put("available", inventory.getAvailable());
      inventoryItem.put("denomination", inventory.getDenomination());
      inventoryItem.put("purchase_price", inventory.getPurchaseprice());

    } catch (JSONException e) {
    }

    visit(new Item(inventory.getItem()));

    inventoryItems.put(inventoryItem);
  }

  public void visit(Item item) {
    try {
      inventoryItem.put("name", item.name());
      inventoryItem.put("amount", item.amount());
      inventoryItem.put("type", item.type().toString());
    } catch (JSONException e) {
    }
  }

  private JSONObject inventoryItem;
  private JSONArray inventoryItems;
}