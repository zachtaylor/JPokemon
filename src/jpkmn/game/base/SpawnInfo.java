package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class SpawnInfo {
  @PrimaryKey
  private int number;
  private int pkmn, min, max, flex;

  private static List<SpawnInfo> getInfo(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<SpawnInfo> info = new SqlStatement().select(SpawnInfo.class)
          .where("number").eq(number).getList();

      return info;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }
}