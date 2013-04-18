package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class NPCActionRequirement implements JPokemonConstants {
  private int number, actionset, requirementset, type, data;

  public static List<NPCActionRequirement> get(int number, int set) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionRequirement.class).where("number").eq(number).and("set").eq(set).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<NPCActionRequirement>();
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getActionset() {return actionset;} public void setActionset(int s) {actionset = s;}
  public int getRequirementset() {return requirementset;} public void setRequirementset(int r) {requirementset = r;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getData() {return data;} public void setData(int d) {data = d;}
  //@format
}