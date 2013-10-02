package com.jpokemon.util.ui.selector;

import java.awt.Component;

import javax.swing.JLabel;

import org.jpokemon.item.Store;

import com.jpokemon.util.ui.JPokemonSelector;

public class StoreSelector extends JPokemonSelector<Store> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    Store store;
    for (int i = 1; (store = Store.get(i)) != null; i++) {
      addElementToModel(store);
    }
  }

  @Override
  protected void renderElement(Component c, Store store) {
    ((JLabel) c).setText("Store#" + store.getId() + " : " + store.getName());
  }

  private static final long serialVersionUID = 1L;
}