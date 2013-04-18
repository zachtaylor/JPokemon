package org.jpokemon.map.npc;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class NPCMap implements JPokemonConstants {
  private int area, npc;

  public static List<NPCMap> get(int area) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(NPCMap.class).where("area").eq(area).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getNpc() {return npc;} public void setNpc(int n) {npc = n;}
  //@format
}