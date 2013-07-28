package com.jpokemon.util.ui.selector;

import org.jpokemon.item.ItemInfo;

import com.jpokemon.util.ui.JPokemonSelector;

public class ItemSelector extends JPokemonSelector<ItemInfo> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    ItemInfo item;
    for (int i = 1; (item = ItemInfo.get(i)) != null; i++) {
      addElementToModel(item);
    }
  }

  private static final long serialVersionUID = 1L;
}