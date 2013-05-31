package com.jpokemon.mapeditor.widget.selector;

import org.jpokemon.overworld.npc.NPC;

public class NPCSelector extends JPokemonSelector<NPC> {
  @Override
  protected void reloadItems() {
    removeAllItems();

    NPC npc;
    for (int i = 1; (npc = NPC.get(i)) != null; i++) {
      addElementToModel(npc);
    }
  }

  private static final long serialVersionUID = 1L;
}