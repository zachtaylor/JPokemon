package com.jpokemon.ui;

import org.jpokemon.pokemon.Type;

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