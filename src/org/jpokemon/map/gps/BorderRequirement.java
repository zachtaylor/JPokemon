package org.jpokemon.map.gps;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.AbstractRequirement;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class BorderRequirement extends AbstractRequirement {
  private int area, next, actionset, type, data;

  public static List<BorderRequirement> get(int area, int next) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(BorderRequirement.class).where("area").eq(area).and("next").eq(next).getList();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void commitTypeChange(int newType) {
    // TODO Auto-generated method stub
  }

  @Override
  public void commitDataChange(int newData) {
    // TODO Auto-generated method stub
  }

  //@preformat
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public int getNext() {return next; } public void setNext(int n) {next = n; }
  public int getActionset() {return actionset; } public void setActionset(int a) {actionset = a; }
  public int getType() {return type; } public void setType(int t) {type = t; }
  public int getData() {return data; } public void setData(int d) {data = d; }
  //@format
}