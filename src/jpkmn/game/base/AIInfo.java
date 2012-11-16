package jpkmn.game.base;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class AIInfo {
  @PrimaryKey
  private int number;

  private int area, cash, type;
  private String name;

  public static AIInfo get(int number) {
    if (cache[number - 1] != null)
      return cache[number - 1];

    DataConnectionManager.init("data/Pokemon.db");

    try {
      List<AIInfo> info = SqlStatement.select(AIInfo.class).where("number")
          .eq(number).getList();

      if (info == null || info.isEmpty())
        return null;

      return cache[number - 1] = info.get(0);
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static List<AIInfo> getAIForArea(int number) {
    DataConnectionManager.init("data/Pokemon.db");

    try {
      List<AIInfo> info = SqlStatement.select(AIInfo.class).where("area")
          .eq(number).getList();

      return info;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getCash() {return cash;} public void setCash(int _val) {cash = _val;}
  public int getNumber() {return number;} public void setNumber(int _val) {number = _val;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public String getName() {return name;} public void setName(String _val) {name = _val;}
  //@format

  private static AIInfo[] cache = new AIInfo[JPokemonConstants.AINUMBER];
}