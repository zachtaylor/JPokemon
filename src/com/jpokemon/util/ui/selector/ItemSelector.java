package com.jpokemon.util.ui.selector;

import java.awt.Component;

import javax.swing.JLabel;

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

  protected void renderElement(Component c, ItemInfo itemInfo) {
    ((JLabel) c).setText("Item#" + itemInfo.getId() + " : " + itemInfo.getName());
  }

  private static final long serialVersionUID = 1L;
}