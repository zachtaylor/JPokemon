package org.jpokemon.map.gps;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.AbstractAction;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class BorderAction extends AbstractAction {
  private int area, next, type;
  private String data;

  public static List<BorderAction> get(int area, int next) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(BorderAction.class).where("area").eq(area).and("next").eq(next).getList();

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
      SqlStatement.update(this).where("area").eq(area).and("next").eq(next).and("type").eq(type).and("data").eq(oldData).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void commitTypeChange(int newType) {
    int oldType = getType();
    setType(newType);

    try {
      SqlStatement.update(this).where("area").eq(area).and("next").eq(next).and("type").eq(oldType).and("data").eq(data).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getNext() {return next;} public void setNext(int n) {next = n;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public String getData() {return data;} public void setData(String d) {data = d;}
  //@format
}