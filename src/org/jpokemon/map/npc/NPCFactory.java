package org.jpokemon.map.npc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jpokemon.action.Action;
import org.jpokemon.action.ActionFactory;
import org.jpokemon.action.ActionSet;
import org.jpokemon.action.Requirement;

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

    for (NPCActionSet actionset : NPCActionSet.get(info.getNumber(), info.getActionset())) {
      Action a = ActionFactory.build(actionset.getType(), actionset.getData());
      as.addAction(a);
    }

    as.setOption(info.getOption());

    for (NPCRequirement npcRequirement : NPCRequirement.get(info.getNumber(), info.getActionset())) {
      as.addRequirement(new Requirement(npcRequirement.getType(), npcRequirement.getData()));
    }

    return as;
  }
}