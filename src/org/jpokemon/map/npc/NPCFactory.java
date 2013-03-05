package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.map.Requirement;

public class NPCFactory {
  public static List<NPC> build(int area) {
    List<NPC> npcs = new ArrayList<NPC>();

    for (NPCInfo info : NPCInfo.get(area)) {
      NPC npc = new NPC(info);
      npc.actions(buildActionSets(area, info.getNumber()));
    }

    return npcs;
  }

  private static List<ActionSet> buildActionSets(int area, int number) {
    Map<Integer, ActionSet> actionsMap = new HashMap<Integer, ActionSet>();

    for (NPCActionInfo actset : NPCActionInfo.get(area, number)) {
      if (actionsMap.get(actset.getSet()) == null)
        actionsMap.put(actset.getSet(), new ActionSet());

      actionsMap.get(actset.getSet()).addAction(new Action(actset.getType(), actset.getData()));
    }

    List<ActionSet> actions = new ArrayList<ActionSet>();
    for (Map.Entry<Integer, ActionSet> actset : actionsMap.entrySet()) {
      actset.getValue().requirements(buildActionSetRequirements(area, number, actset.getKey()));
      actions.add(actset.getValue());
    }

    return actions;
  }

  private static List<List<Requirement>> buildActionSetRequirements(int area, int number, int set) {
    Map<Integer, List<Requirement>> requirementMaps = new HashMap<Integer, List<Requirement>>();

    for (NPCActionRequirement req : NPCActionRequirement.get(area, number, set)) {
      if (requirementMaps.get(req.getReqset()) == null)
        requirementMaps.put(req.getReqset(), new ArrayList<Requirement>());

      requirementMaps.get(req.getReqset()).add(new Requirement(req.getType(), req.getData()));
    }

    List<List<Requirement>> requirements = new ArrayList<List<Requirement>>();
    for (Map.Entry<Integer, List<Requirement>> reqList : requirementMaps.entrySet()) {
      requirements.add(reqList.getValue());
    }

    return requirements;
  }
}