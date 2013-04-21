package org.jpokemon.map.npc;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class NPCActionSetInfo implements JPokemonConstants {
  private int number, actionset;
  private String option;

  public static NPCActionSetInfo get(int number, int actionset) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      List<NPCActionSetInfo> query = SqlStatement.select(NPCActionSetInfo.class).where("number").eq(number).and("actionset").eq(actionset).getList();

      if (query.size() > 0)
        return query.get(0);
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number=n;}
  public int getActionset() {return actionset;} public void setActionset(int a) {actionset=a;}
  public String getOption() {return option;} public void setOption(String o) {option = o;}
  //@format
}