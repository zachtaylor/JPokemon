package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.map.Area;

public class AreaSelector extends JPokemonSelector<Area> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    Area area;
    for (int i = 1; (area = Area.get(i)) != null; i++) {
      addElementToModel(area);
    }
  }
}