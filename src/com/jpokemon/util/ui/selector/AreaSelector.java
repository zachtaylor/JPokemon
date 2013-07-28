package com.jpokemon.util.ui.selector;

import org.jpokemon.overworld.map.Area;

import com.jpokemon.util.ui.JPokemonSelector;

public class AreaSelector extends JPokemonSelector<Area> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    Area area;
    for (int i = 1; (area = Area.get(i)) != null; i++) {
      addElementToModel(area);
    }
  }

  private static final long serialVersionUID = 1L;
}