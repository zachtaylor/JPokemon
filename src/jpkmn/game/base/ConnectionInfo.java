package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class ConnectionInfo {
  @PrimaryKey
  private int number;
  private int direction, next, requirement, value;

  public static List<ConnectionInfo> get(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<ConnectionInfo> info = new SqlStatement()
          .select(ConnectionInfo.class).where("number").eq(number).getList();

      return info;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getDirection() {return direction;} public void setDirection(int d) {direction = d;}
  public int getNext() {return next;} public void setNext(int n) {next = n;}
  public int getRequirement() {return requirement;} public void setRequirement(int r) {requirement = r;}
  public int getValue() {return value;} public void setValue(int v) {value = v;}
  //@format
}