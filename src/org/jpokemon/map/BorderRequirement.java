package org.jpokemon.map;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class BorderRequirement implements JPokemonConstants {
  private int area, next, type, data;

  public static List<BorderRequirement> get(int area, int next) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(BorderRequirement.class).where("area").eq(area).and("next").eq(next).getList();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public int getNext() {return next; } public void setNext(int n) {next = n; }
  public int getType() {return type; } public void setType(int t) {type = t; }
  public int getData() {return data; } public void setData(int d) {data = d; }
  //@format
}