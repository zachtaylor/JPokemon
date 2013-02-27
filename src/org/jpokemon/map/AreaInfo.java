package org.jpokemon.map;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class AreaInfo implements JPokemonConstants {
  @PrimaryKey
  private int area;
  private String name;

  private static Map<Integer, AreaInfo> cache = new HashMap<Integer, AreaInfo>();

  public static AreaInfo get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    if (cache.get(number) == null) {
      try {
        List<AreaInfo> info = SqlStatement.select(AreaInfo.class).where("area").eq(number).getList();

        if (info.size() > 0)
          cache.put(number, info.get(0));

      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }

  //@preformat
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public String getName() {return name; } public void setName(String n) {name = n; }
  //@format
}