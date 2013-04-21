package org.jpokemon.map;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class WildPokemon implements JPokemonConstants {
  private int area, number, levelmin, levelmax, flex;

  public Pokemon instantiate() {
    int level = (int) ((levelmax - levelmin + 1) * Math.random()) + levelmin;

    return new Pokemon(number, level);
  }

  public static List<WildPokemon> get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      return SqlStatement.select(WildPokemon.class).where("area").eq(number).getList();

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area; } public void setArea(int a) {area = a; }
  public int getNumber() {return number; } public void setNumber(int n) {number = n; }
  public int getLevelmin() {return levelmin; } public void setLevelmin(int l) {levelmin = l; }
  public int getLevelmax() {return levelmax; } public void setLevelmax(int l) {levelmax = l; }
  public int getFlex() {return flex; } public void setFlex(int f) {flex = f; }
  //@format
}
