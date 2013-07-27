package org.jpokemon.overworld.map;

import java.util.List;

import org.jpokemon.action.AbstractRequirement;
import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class BorderRequirement extends AbstractRequirement {
  private int area, next, type, data;

  public static List<BorderRequirement> get(int area, int next) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      return SqlStatement.select(BorderRequirement.class).where("area").eq(area).and("next").eq(next).getList();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  @Override
  public void commitDataChange(int newData) {
    int oldData = getData();
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
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public int getNext() {return next; } public void setNext(int n) {next = n; }
  public int getType() {return type; } public void setType(int t) {type = t; }
  public int getData() {return data; } public void setData(int d) {data = d; }
  //@format
}