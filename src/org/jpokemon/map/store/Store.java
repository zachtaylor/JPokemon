package org.jpokemon.map.store;

import java.util.List;
import java.util.Iterator;

public class Store implements Iterable<Inventory> {
  private Store(int number) {
    _number = number;
    _inventory = Inventory.get(number);
  }

  public static Store get(int number) {
    Store store = new Store(number);

    if (store.isEmpty()) {
      store = null;
    }

    return store;
  }

  public int getNumber() {
    return _number;
  }

  public boolean isEmpty() {
    return _inventory.isEmpty();
  }

  public String toString() {
    return "Store#" + _number;
  }

  @Override
  public Iterator<Inventory> iterator() {
    return _inventory.iterator();
  }

  private int _number;
  private List<Inventory> _inventory;
}