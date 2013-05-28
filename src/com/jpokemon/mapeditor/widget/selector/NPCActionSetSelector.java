package com.jpokemon.mapeditor.widget.selector;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.map.npc.NPCActionSetMap;

public class NPCActionSetSelector extends JPokemonSelector<NPCActionSetMap> {
  public NPCActionSetSelector(int n) {
    npcNumber = n;
  }

  public void setNPCNumber(int n) {
    npcNumber = n;
  }

  @Override
  protected void reloadItems() {
    removeAllItems();

    List<Integer> seenActionSetNumbers = new ArrayList<Integer>();

    for (NPCActionSetMap npcActionSetMap : NPCActionSetMap.getByNPCNumber(npcNumber)) {
      if (seenActionSetNumbers.contains(npcActionSetMap.getActionset())) {
        continue;
      }

      seenActionSetNumbers.add(npcActionSetMap.getActionset());
      addElementToModel(npcActionSetMap);
    }
  }

  private int npcNumber;

  private static final long serialVersionUID = 1L;
}