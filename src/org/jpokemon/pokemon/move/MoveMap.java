package org.jpokemon.pokemon.move;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class MoveMap implements JPokemonConstants {
  private int pokemon_number, pokemon_level, move_number;

  private static Map<Integer, Map<Integer, List<MoveMap>>> cache = new HashMap<Integer, Map<Integer, List<MoveMap>>>();

  public static List<MoveMap> get(int number, int level) {
    DataConnectionManager.init(DATABASE_PATH);

    ensureCacheExists(number);
    if (cache.get(number).get(level) == null) {
      try {
        List<MoveMap> maps = SqlStatement.select(MoveMap.class)
            .where("pokemon_number").eq(number).and("pokemon_level").eq(level)
            .getList();

        cache.get(number).put(level, maps);
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }
    return cache.get(number).get(level);
  }

  private static void ensureCacheExists(int number) {
    if (cache.get(number) == null)
      cache.put(number, new HashMap<Integer, List<MoveMap>>());
  }

  //@preformat
  public int getPokemon_number() { return pokemon_number; } public void setPokemon_number(int val) { pokemon_number = val; }
  public int getPokemon_level() { return pokemon_level; } public void setPokemon_level(int val) { pokemon_level = val; }
  public int getMove_number() { return move_number; } public void setMove_number(int val) { move_number = val; }
  //@format
}