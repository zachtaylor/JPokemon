package org.jpokemon.manager.component;

import org.jpokemon.item.Item;
import org.jpokemon.manager.Activity;
import org.jpokemon.manager.JPokemonServer;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.map.gps.Map;
import org.jpokemon.map.store.Inventory;
import org.jpokemon.map.store.Store;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreServer extends JPokemonServer {
  public StoreServer(Player player) {
    super(player);

    visit(player);
  }

  public void visit(Player player) {
    Activity activity = PlayerManager.getActivity(player);

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