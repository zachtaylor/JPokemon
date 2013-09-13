package org.jpokemon.overworld.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

/**
 * A type of NPC (Bug Catcher, Lass, etc). NpcType will distinguish the way a
 * NPCs name is displayed, and they way they appear.
 */
public class NpcType {
  private String name, icon;

  private static Map<String, NpcType> cache = new HashMap<String, NpcType>();

  public static NpcType get(String name) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    if (cache.get(name) == null) {
      try {
        List<NpcType> info = SqlStatement.select(NpcType.class).where("name").eq(name).getList();

        if (info.size() > 0) {
          cache.put(name, info.get(0));
        }
      }
      catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(name);
  }

  public static NpcType createNew() {
    NpcType NpcType = new NpcType();
    NpcType.setName("undefined");
    NpcType.setIcon("undefined");

    try {
      SqlStatement.insert(NpcType).execute();
    }
    catch (DataConnectionException e) {
      NpcType = null;
      e.printStackTrace();
    }

    return NpcType;
  }

  public void commit() {
    try {
      SqlStatement.update(this).execute();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  public String toString() {
    return "NpcType:" + name;
  }

  //@preformat
 public String getName() {return name;} public void setName(String n) {name = n;}
 public String getIcon() {return icon;} public void setIcon(String i) {icon = i;}
 //@format
}