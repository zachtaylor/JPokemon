package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class AIParty {
  private int ai_number;
  private String entry;

  public static List<AIParty> get(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<AIParty> data = new SqlStatement().select(AIParty.class)
          .where("ai_number").eq(number).getList();

      return data;

    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getAi_number() {return ai_number;} public void setAi_number(int _n) {ai_number = _n;}
  public String getEntry() {return entry;} public void setEntry(String e) {entry = e;}
  //@format
}