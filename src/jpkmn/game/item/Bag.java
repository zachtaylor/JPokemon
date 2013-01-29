package jpkmn.game.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;

import org.json.JSONArray;

public class Bag {
  public Bag() {
    _items = new HashMap<Integer, Item>();
  }

  public Item get(int itemID) {
    Item item = _items.get(itemID);

    if (item == null)
      _items.put(itemID, item = new Item(itemID));

    return item;
  }

  public JSONArray toJSON() {
    JSONArray data = new JSONArray();

    for (Item item : _items.values())
      if (item.visible()) data.put(item.toJSON());

    return data;
  }

  public void load(String s) throws LoadException {
    try {
      if (!s.startsWith("BAG: ")) throw new Exception();

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

  private Map<Integer, Item> _items;
}