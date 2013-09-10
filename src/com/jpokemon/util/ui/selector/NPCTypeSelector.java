package com.jpokemon.util.ui.selector;

import com.jpokemon.util.ui.JPokemonSelector;

public class NPCTypeSelector extends JPokemonSelector<NPCType> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    NPCType npcType;
    for (int i = 1; (npcType = NPCType.get(i)) != null; i++) {
      addElementToModel(npcType);
    }
  }

  private static final long serialVersionUID = 1L;
}