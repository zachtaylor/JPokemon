package org.jpokemon.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.map.npc.NPCFactory;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class Map implements JPokemonConstants {
  private static HashMap<Integer, Area> map = new HashMap<Integer, Area>();

  public static Area area(int id) {
    if (map.get(id) == null)
      load(id);

    return map.get(id);
  }

  private static void load(int id) {
    Area a = loadArea(id);

    for (Border b : loadMap(id)) {
      for (BorderRequirement br : BorderRequirement.get(id, b.getNext()))
        b.addRequirement(br);

      a.addBorder(b);
    }

    for (WildPokemon wp : WildPokemon.get(id))
      a.addPokemon(wp);

    for (NPC npc : NPCFactory.build(id))
      a.addNPC(npc);

    map.put(id, a);
  }

  private static List<Border> loadMap(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(Border.class).where("area").eq(number).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<Border>();
  }

  private static Area loadArea(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      List<Area> info = SqlStatement.select(Area.class).where("area").eq(number).getList();

      if (info.size() > 0)
        return info.get(0);

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }
}