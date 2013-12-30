package com.jpokemon.util.ui.selector;

import com.jpokemon.util.ui.JPokemonSelector;

public class ActionTypeSelector extends JPokemonSelector<ActionType> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    for (ActionType actionType : ActionType.values()) {
      addElementToModel(actionType);
    }
  }

  private static final long serialVersionUID = 1L;
}