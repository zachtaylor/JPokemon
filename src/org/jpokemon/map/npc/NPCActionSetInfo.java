package org.jpokemon.map.npc;

import java.util.List;
import org.jpokemon.JPokemonConstants;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class NPCActionSetInfo {
  private int number, area, actionset;
  private String option;

  public static List<NPCActionSetInfo> get(int area) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(NPCActionSetInfo.class).where("area").eq(area).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number=n;}
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getActionset() {return actionset;} public void setActionset(int a) {actionset=a;}
  public String getOption() {return option;} public void setOption(String o) {option = o;}
  //@format
}