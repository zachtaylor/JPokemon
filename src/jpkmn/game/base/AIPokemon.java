package jpkmn.game.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class AIPokemon implements JPokemonConstants {
  private int ai_number;
  private String entry;

  private static Map<Integer, List<AIPokemon>> cache = new HashMap<Integer, List<AIPokemon>>();

  public static List<AIPokemon> get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    if (number < 0 || number >= JPokemonConstants.AINUMBER)
      throw new IllegalArgumentException("AI number out of range: " + number);

    if (cache.get(number) == null) {
      try {
        cache.put(number,
            SqlStatement.select(AIPokemon.class).where("ai_number").eq(number)
                .getList());
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }

  //@preformat
  public int getAi_number() {return ai_number;} public void setAi_number(int _n) {ai_number = _n;}
  public String getEntry() {return entry;} public void setEntry(String e) {entry = e;}
  //@format
}