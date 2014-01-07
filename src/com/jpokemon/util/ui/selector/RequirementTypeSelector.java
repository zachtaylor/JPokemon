package com.jpokemon.util.ui.selector;

import com.jpokemon.util.ui.JPokemonSelector;

public class RequirementTypeSelector extends JPokemonSelector<String> {
  @Override
  @SuppressWarnings("unchecked")
  protected void reloadItems() {
    removeAllItems();

    addItem("undefined");
    addItem("event");
    addItem("pokedex");
  }

  private static final long serialVersionUID = 1L;
}