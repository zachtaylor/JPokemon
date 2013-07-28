package com.jpokemon.util.ui.selector;

import org.jpokemon.overworld.npc.NPC;

import com.jpokemon.util.ui.JPokemonSelector;

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