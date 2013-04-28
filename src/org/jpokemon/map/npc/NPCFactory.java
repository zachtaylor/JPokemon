package org.jpokemon.map.npc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jpokemon.action.Action;
import org.jpokemon.action.ActionFactory;
import org.jpokemon.action.ActionSet;
import org.jpokemon.action.Requirement;
import org.jpokemon.action.RequirementSet;

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

    for (RequirementSet set : buildRequirementSets(info.getNumber(), info.getActionset())) {
      as.addRequirements(set);
    }

    return as;
  }

  private static Collection<RequirementSet> buildRequirementSets(int number, int actionset) {
    Map<Integer, RequirementSet> requirementsets = new HashMap<Integer, RequirementSet>();

    for (NPCRequirement req : NPCRequirement.get(number, actionset)) {
      RequirementSet requirementset = requirementsets.get(req.getRequirementset());

      if (requirementset == null) {
        requirementsets.put(req.getRequirementset(), requirementset = new RequirementSet());
      }

      requirementset.add(new Requirement(req.getType(), req.getData()));
    }

    return requirementsets.values();
  }
}