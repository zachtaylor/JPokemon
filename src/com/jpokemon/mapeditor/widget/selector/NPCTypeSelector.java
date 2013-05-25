package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.map.npc.NPCType;

public class NPCTypeSelector extends JPokemonSelector<NPCType> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    NPCType npcType;
    for (int i = 1; (npcType = NPCType.get(i)) != null; i++) {
      addElementToModel(npcType);
    }
  }
}