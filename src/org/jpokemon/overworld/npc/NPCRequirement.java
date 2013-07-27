package org.jpokemon.overworld.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.action.AbstractRequirement;
import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCRequirement extends AbstractRequirement {
  private int number, actionset, type, data;

  public static List<NPCRequirement> get(int number, int set) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      return SqlStatement.select(NPCRequirement.class).where("number").eq(number).and("actionset").eq(set).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<NPCRequirement>();
  }

  @Override
  public void commitDataChange(int newData) {
    int oldData = getData();
    setData(newData);

    try {
      SqlStatement.update(this).where("number").eq(number).and("actionset").eq(actionset).and("type").eq(type).and("data").eq(oldData).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void commitTypeChange(int newType) {
    int oldType = getType();
    setType(newType);

    try {
      SqlStatement.update(this).where("number").eq(number).and("actionset").eq(actionset).and("type").eq(oldType).and("data").eq(data).execute();
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