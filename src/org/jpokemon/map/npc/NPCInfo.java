package org.jpokemon.map.npc;

import java.util.List;
import org.jpokemon.JPokemonConstants;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCInfo {
  private int number, type, avoidprefix;
  private String name;

  public static NPCInfo get(int number) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      List<NPCInfo> query = SqlStatement.select(NPCInfo.class).where("number").eq(number).getList();

      if (query.size() > 0)
        return query.get(0);
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public boolean usePrefix() {
    return avoidprefix == 0;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getAvoidprefix() {return avoidprefix;} public void setAvoidprefix(int a) {avoidprefix=a;}
  public String getName() {return name;} public void setName(String n) {name = n;}
  //@format
}