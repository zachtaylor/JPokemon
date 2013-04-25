package org.jpokemon.map.npc;

import java.util.List;
import org.jpokemon.JPokemonConstants;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCActionMapping {
  private int number, actionset, type;
  private String data;

  public static List<NPCActionMapping> get(int number, int actionset) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionMapping.class).where("number").eq(number).and("actionset").eq(actionset).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getActionset() {return actionset;} public void setActionset(int s) {actionset = s;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public String getData() {return data;} public void setData(String d) {data = d;}
  //@format
}