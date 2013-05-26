package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.List;
import org.jpokemon.JPokemonConstants;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCRequirement {
  private int number, actionset, type, data;

  public static List<NPCRequirement> get(int number, int set) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(NPCRequirement.class).where("number").eq(number).and("actionset").eq(set).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<NPCRequirement>();
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getActionset() {return actionset;} public void setActionset(int s) {actionset = s;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getData() {return data;} public void setData(int d) {data = d;}
  //@format
}