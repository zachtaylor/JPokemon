package org.jpokemon.map;

import java.util.HashMap;
import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class Map implements JPokemonConstants {
  private int area, next;
  private static HashMap<Integer, List<Map>> cache = new HashMap<Integer, List<Map>>();
  private static HashMap<Integer, Area> map = new HashMap<Integer, Area>();

  public static Area area(int id) {
    if (map.get(id) == null)
      load(id);

    return map.get(id);
  }

  private static void load(int id) {
    Area a = new Area(id);

    for (Map map : get(id)) {
      Border b = new Border(map.getNext());

      for (MapRequirement mr : MapRequirement.get(id, map.getNext()))
        b.addRequirement(new Requirement(mr.getType(), mr.getData()));

      a.addBorder(b);
    }

  }

  private static List<Map> get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    if (cache.get(number) == null) {
      try {
        cache.put(number, SqlStatement.select(Map.class).where("area").eq(number).getList());

      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }

  //@preformat
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public int getNext() {return next; } public void setNext(int n) {next = n; }
  //@format
}