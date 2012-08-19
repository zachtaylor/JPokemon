package jpkmn.game.item;

import java.util.ArrayList;
import java.util.List;

public class BagPocket {
  public BagPocket() {
    _items = new ArrayList<Item>();
  }

  public Item get(int num) {
    return _items.get(num);
  }

  public void add(Item i) {
    _items.add(i);
  }

  private List<Item> _items;
}