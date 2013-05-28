package org.jpokemon.map.npc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jpokemon.action.ActionSet;

public class NPCFactory {
  public static Collection<NPC> build(int area) {
    Map<Integer, NPC> npcs = new HashMap<Integer, NPC>();

    // Get all in this area
    for (NPCActionSetMap npcMapping : NPCActionSetMap.get(area)) {
      NPC npc = npcs.get(npcMapping.getNumber());

      if (npc == null) {
        npcs.put(npcMapping.getNumber(), npc = NPC.get(npcMapping.getNumber()));
      }

      ActionSet actionset = buildActionSet(npcMapping);
      npc.addActionSet(actionset);
    }

    return npcs.values();
  }

  private static ActionSet buildActionSet(NPCActionSetMap info) {
    ActionSet as = new ActionSet();

    as.setOption(info.getOption());

    for (NPCAction action : NPCAction.get(info.getNumber(), info.getActionset())) {
      as.addAction(action);
    }
    for (NPCRequirement npcRequirement : NPCRequirement.get(info.getNumber(), info.getActionset())) {
      as.addRequirement(npcRequirement);
    }

    return as;
  }
}