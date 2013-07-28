package com.jpokemon.util.ui.selector;

import org.jpokemon.pokemon.Type;

import com.jpokemon.util.ui.JPokemonSelector;

public class TypeSelector extends JPokemonSelector<Type> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    for (Type type : Type.values()) {
      addElementToModel(type);
    }
  }

  private static final long serialVersionUID = 1L;
}