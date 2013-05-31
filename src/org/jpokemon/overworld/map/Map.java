package org.jpokemon.overworld.map;

import java.util.HashMap;

import org.jpokemon.overworld.npc.NPC;
import org.jpokemon.overworld.npc.NPCFactory;

public class Map {
  private static HashMap<Integer, Area> map = new HashMap<Integer, Area>();

  public static Area area(int id) {
    if (map.get(id) == null)
      load(id);

    return map.get(id);
  }

  private static void load(int number) {
    Area a = Area.get(number);

    if (a == null)
      return;

    for (Border b : Border.get(number)) {
      for (BorderAction borderAction : BorderAction.get(b.getArea(), b.getNext())) {
        b.addAction(borderAction);
      }
      for (BorderRequirement borderRequirement : BorderRequirement.get(b.getArea(), b.getNext())) {
        b.addRequirement(borderRequirement);
      }

      a.addBorder(b);
    }

    for (WildPokemon wp : WildPokemon.get(number))
      a.addPokemon(wp);

    for (NPC npc : NPCFactory.build(number))
      a.addNPC(npc);

    map.put(number, a);
  }
}