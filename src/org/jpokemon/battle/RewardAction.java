package org.jpokemon.battle;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.AbstractAction;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class RewardAction extends AbstractAction {
  private int type;
  private String trainerid, data;

  public static List<RewardAction> get(String trainerID) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(RewardAction.class).where("trainerid").eq(trainerID).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void commitTypeChange(int t) {
    // TODO Auto-generated method stub
  }

  @Override
  public void commitDataChange(String newData) {
    // TODO Auto-generated method stub
  }

  //@preformat
  public String getTrainerid() {return trainerid;} public void setTrainerid(String t) {trainerid = t;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public String getData() {return data;} public void setData(String d) {data = d;}
  //@format

}