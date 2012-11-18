package jpkmn.game.base;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class AreaInfo implements JPokemonConstants {
  public static AreaInfo getInfo(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      List<AreaInfo> info = SqlStatement.select(AreaInfo.class)
          .where("number").eq(number).getList();

      return info.isEmpty() ? null : info.get(0);
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }
}