package org.jpokemon.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class MapRequirement implements JPokemonConstants {
  private int area, next, type, data;

  private static Map<Integer, Map<Integer, List<MapRequirement>>> cache = new HashMap<Integer, Map<Integer, List<MapRequirement>>>();

  public static List<MapRequirement> get(int area, int next) {
    DataConnectionManager.init(DATABASE_PATH);

    ensureInitialized(area);

    if (cache.get(area).get(next) == null) {
      try {
        List<MapRequirement> info = SqlStatement.select(MapRequirement.class).where("area").eq(area).and("next")
            .eq(next).getList();

        cache.get(area).put(next, info);
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(area).get(next);
  }

  private static void ensureInitialized(int area) {
    if (cache.get(area) == null)
      cache.put(area, new HashMap<Integer, List<MapRequirement>>());
  }

  //@preformat
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public int getNext() {return next; } public void setNext(int n) {next = n; }
  public int getType() {return type; } public void setType(int t) {type = t; }
  public int getData() {return data; } public void setData(int d) {data = d; }
  //@format
}