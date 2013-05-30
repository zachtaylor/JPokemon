package org.jpokemon.overworld.npc;

import java.util.List;
import org.jpokemon.JPokemonConstants;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCActionSetMap {
  private int number, area, actionset;
  private String option;

  public static List<NPCActionSetMap> get(int area) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionSetMap.class).where("area").eq(area).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static List<NPCActionSetMap> getByNPCNumber(int npcNumber) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionSetMap.class).where("number").eq(npcNumber).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public String toString() {
    return "NPCActionSet#" + getActionset() + " " + getOption();
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number=n;}
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getActionset() {return actionset;} public void setActionset(int a) {actionset=a;}
  public String getOption() {return option;} public void setOption(String o) {option = o;}
  //@format
}