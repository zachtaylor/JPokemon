package org.jpokemon.overworld.npc;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.AbstractAction;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCAction extends AbstractAction {
  private int number, actionset;
  private String type, data;

  public static List<NPCAction> get(int number, int actionset) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(NPCAction.class).where("number").eq(number).and("actionset").eq(actionset).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void commitDataChange(String newData) {
    String oldData = getData();
    setData(newData);

    try {
      SqlStatement.update(this).where("number").eq(getNumber()).and("actionset").eq(getActionset()).and("type").eq(getType()).and("data").eq(oldData).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void commitTypeChange(String newType) {
    String oldType = getType();
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
  public String getType() {return type;} public void setType(String t) {type = t;}
  public String getData() {return data;} public void setData(String d) {data = d;}
  //@format
}