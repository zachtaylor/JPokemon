package jpkmn.game.item;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BagPocket implements Iterable<Item> {
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

  @Override
  public Iterator<Item> iterator() {
    return new BagPocketIterator();
  }

  private class BagPocketIterator implements Iterator<Item> {
    @Override
    public boolean hasNext() {
      while (_index < _items.size() && _items.get(_index).amount() == 0)
        ++_index;

      if (_index == _items.size()) return false;
      return true;
    }

    @Override
    public Item next() {
      return _items.get(_index++);
    }

    @Override
    public void remove() {
      // Mm... nope.
    }

    private int _index;
  }
}