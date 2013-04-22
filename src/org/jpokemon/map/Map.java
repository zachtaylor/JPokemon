package org.jpokemon.map;

import java.util.HashMap;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.map.npc.NPCFactory;

public class Map implements JPokemonConstants {
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
      for (BorderRequirement br : BorderRequirement.get(b.getArea(), b.getNext())) {
        Requirement r = new Requirement(br.getType(), br.getData());
        b.addRequirement(r);
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