package org.jpokemon.map.npc;

import java.util.List;
import org.jpokemon.JPokemonConstants;
import org.jpokemon.map.ActionSetEntry;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCActionSet implements ActionSetEntry {
  private int number, actionset, type;
  private String data;

  public static List<NPCActionSet> get(int number, int actionset) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionSet.class).where("number").eq(number).and("actionset").eq(actionset).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void commitDataChange(String newData) {
    String oldData = getData();
    setData(newData);

    try {
      SqlStatement.update(this).where("number").eq(getNumber()).and("actionset").eq(getActionset()).and("type").eq(getType()).and("data").eq(oldData).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  public void commitTypeChange(int newType) {
    int oldType = getType();
    setType(newType);

    try {
      SqlStatement.update(this).where("number").eq(getNumber()).and("actionset").eq(getActionset()).and("type").eq(oldType).and("data").eq(getData()).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getActionset() {return actionset;} public void setActionset(int s) {actionset = s;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public String getData() {return data;} public void setData(String d) {data = d;}
  //@format
}