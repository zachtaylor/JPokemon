package jpkmn.game.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;

import org.jpokemon.exception.ConfigurationException;
import org.json.JSONArray;

public class Bag {
  public Bag() {
    _allItems = new ArrayList<Item>();
    _pockets = new HashMap<ItemType, BagPocket>();

    for (ItemType type : ItemType.values())
      _pockets.put(type, new BagPocket());

    Item newItem;
    try {
      for (int id = 1; (newItem = new Item(id)) != null; id++) {
        _allItems.add(newItem);
        _pockets.get(newItem.type()).add(newItem);
      }
    } catch (ConfigurationException c) { // End items
    }
  }

  public Item get(int itemID) {
    return _allItems.get(itemID);
  }

  public Iterable<Item> pocket(ItemType type) {
    return _pockets.get(type);
  }

  public JSONArray toJSONArray() {
    JSONArray data = new JSONArray();

    for (Item item : _allItems)
      data.put(item.toJSONObject());

    return data;
  }

  public void load(String s) throws LoadException {
    try {
      if (!s.startsWith("BAG: "))
        throw new Exception();

      Scanner scan = new Scanner(s);
      scan.next(); // Throw away "BAG: "

      String[] parts;
      while (scan.hasNext()) {
        parts = scan.next().split("-");
        get(Integer.parseInt(parts[0])).amount(Integer.parseInt(parts[1]));
      }
      scan.close();
    } catch (Exception e) {
      throw new LoadException("Bag could not load: " + s);
    }
  }

  private List<Item> _allItems;
  private Map<ItemType, BagPocket> _pockets;
}