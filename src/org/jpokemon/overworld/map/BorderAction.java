package org.jpokemon.overworld.map;

import java.util.List;

import org.jpokemon.action.AbstractAction;
import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class BorderAction extends AbstractAction {
  private int area, next;
  private String type, data;

  public static List<BorderAction> get(int area, int next) {
    DataConnectionManager.init(JPokemonServer.databasepath);

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
  public void commitTypeChange(String newType) {
    String oldType = getType();
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
  public String getType() {return type;} public void setType(String t) {type = t;}
  public String getData() {return data;} public void setData(String d) {data = d;}
  //@format
}