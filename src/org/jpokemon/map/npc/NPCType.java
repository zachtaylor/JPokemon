package org.jpokemon.map.npc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

/**
 * A type of NPC (Bug Catcher, Lass, etc). NPCType will distinguish the way a
 * NPCs name is displayed, and they way they appear.
 */
public class NPCType {
  @PrimaryKey
  @AutoIncrement
  private int number;

  private String name, icon;

  private static Map<Integer, NPCType> cache = new HashMap<Integer, NPCType>();

  public static NPCType get(int number) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    if (cache.get(number) == null) {
      try {
        List<NPCType> info = SqlStatement.select(NPCType.class).where("number").eq(number).getList();

        if (info.size() > 0) {
          cache.put(number, info.get(0));
        }
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }

  public static NPCType createNew() {
    NPCType npcType = new NPCType();
    npcType.setName("undefined");
    npcType.setIcon("undefined");

    try {
      SqlStatement.insert(npcType).execute();
    } catch (DataConnectionException e) {
      npcType = null;
      e.printStackTrace();
    }

    return npcType;
  }

  public void commit() {
    try {
      SqlStatement.update(this).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  public String toString() {
    return "NPCType#" + number + ": " + getName();
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public String getName() {return name;} public void setName(String n) {name = n;}
  public String getIcon() {return icon;} public void setIcon(String i) {icon = i;}
  //@format
}