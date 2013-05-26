package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.RequirementSetEntry;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCRequirement implements RequirementSetEntry {
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

  public void commitDataChange(int newData) {
    int oldData = getData();
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
  public int getData() {return data;} public void setData(int d) {data = d;}
  //@format
}