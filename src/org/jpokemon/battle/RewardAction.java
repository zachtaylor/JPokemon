package org.jpokemon.battle;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.AbstractAction;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class RewardAction extends AbstractAction {
  private String trainerid, type, data;

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
  public void commitDataChange(String newData) {
    String oldData = getData();
    setData(newData);

    try {
      if (oldData == null) {
        oldData = "";
      }

      SqlStatement.update(this).where("trainerid").eq(trainerid).and("type").eq(getType()).and("data").eq(oldData).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void commitTypeChange(String newType) {
    String oldType = getType();
    setType(newType);

    try {
      SqlStatement.update(this).where("trainerid").eq(trainerid).and("type").eq(oldType).and("data").eq(getData()).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  //@preformat
  public String getTrainerid() {return trainerid;} public void setTrainerid(String t) {trainerid = t;}
  public String getType() {return type;} public void setType(String t) {type = t;}
  public String getData() {return data;} public void setData(String d) {data = d;}
  //@format
}