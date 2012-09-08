package jpkmn.game.base;

import java.util.List;

import jpkmn.Constants;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class AIInfo {
  @PrimaryKey
  private int number;
  private int cash;
  private String name;

  public static AIInfo getInfoForLeader(int number) {
    if (number <= 0 || number > Constants.GYMNUMBER) return null;
    return doGet(number);
  }

  public static AIInfo getInfoForRival(int number) {
    if (number <= 0 || number > Constants.RIVALNUMBER) return null;
    return doGet(number + Constants.GYMNUMBER);
  }

  public static AIInfo getInfoForTrainer(int number) {
    if (number <= 0 || number > Constants.TRAINERNUMBER) return null;
    return doGet(number + Constants.GYMNUMBER + Constants.RIVALNUMBER);
  }

  private static AIInfo doGet(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<AIInfo> info = new SqlStatement().select(AIInfo.class)
          .where("number").eq(number).getList();

      return info.isEmpty() ? null : info.get(0);
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int _val) {number = _val;}
  public int getCash() {return cash;} public void setCash(int _val) {cash = _val;}
  public String getName() {return name;} public void setName(String _val) {name = _val;}
  //@format
}