package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.item.Store;

public class StoreSelector extends JPokemonSelector<Store> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    Store store;
    for (int i = 1; (store = Store.get(i)) != null; i++) {
      addElementToModel(store);
    }
  }

  private static final long serialVersionUID = 1L;
}