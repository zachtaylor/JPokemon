package jpkmn.game.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;

public class Bag {
  public Bag() {
    _allItems = new Item[Constants.ITEMNUMBER];
    _pockets = new HashMap<ItemType, BagPocket>();

    for (ItemType type : ItemType.values())
      _pockets.put(type, new BagPocket());

    for (int id = 1; id <= Constants.ITEMNUMBER; id++) {
      _allItems[id - 1] = new Item(id);
      _pockets.get(_allItems[id - 1].type()).add(_allItems[id - 1]);
    }
  }

  public Item get(int itemID) {
    return _allItems[itemID - 1];
  }

  public Iterable<Item> pocket(ItemType type) {
    return _pockets.get(type);
  }

  public String save() {
    StringBuilder data = new StringBuilder();

    data.append("BAG: ");

    for (Item item : _allItems) {
      if (item.amount() == 0)
        continue;

      data.append(item.toString());
      data.append(" ");
    }

    data.append("\n");

    return data.toString();
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

  private Item[] _allItems;
  private Map<ItemType, BagPocket> _pockets;
}