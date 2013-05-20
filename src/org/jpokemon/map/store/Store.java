package org.jpokemon.map.store;

import java.util.List;
import java.util.Iterator;

public class Store implements Iterable<Inventory> {
  private Store() {
  }

  public static Store get(int number) {
    Store store = new Store();
    store._inventory = Inventory.get(number);

    return store;
  }

  @Override
  public Iterator<Inventory> iterator() {
    return _inventory.iterator();
  }

  private List<Inventory> _inventory;
}