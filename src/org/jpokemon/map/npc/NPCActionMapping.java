package org.jpokemon.map.npc;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class NPCActionMapping implements JPokemonConstants {
  private int number, actionset, type;
  private String data;

  public static List<NPCActionMapping> get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionMapping.class).where("number").eq(number).getList();

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