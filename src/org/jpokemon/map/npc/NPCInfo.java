package org.jpokemon.map.npc;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class NPCInfo implements JPokemonConstants {
  private int area, number, type;
  private String name;

  public static List<NPCInfo> get(int area) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(NPCInfo.class).where("area").eq(area).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public String getName() {return name;} public void setName(String n) {name = n;}
  //@format
}