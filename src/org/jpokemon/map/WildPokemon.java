package org.jpokemon.map;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class WildPokemon implements JPokemonConstants {
  private int area, number, levelmin, levelmax, flex;
  
  private static Map<Integer, List<WildPokemon>> cache = new HashMap<Integer, List<WildPokemon>>();
  
  public static List<WildPokemon> get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    if (cache.get(number) == null) {
      try {
        List<WildPokemon> info = SqlStatement.select(WildPokemon.class).where("area").eq(number).getList();

        if (info.size() > 0)
          cache.put(number, info);

      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }
  
  //@preformat
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public int getNumber() {return number; } public void setNumber(int n) {number = n; }
  public int getLevelmin() {return levelmin; } public void setLevelmin(int l) {levelmin = l; }
  public int getLevelmax() {return levelmax; } public void setLevelmax(int l) {levelmax = l; }
  public int getFlex() {return flex; } public void setFlex(int f) {flex = f; }
  //@format
}
