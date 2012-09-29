package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class ConnectionInfo {
  private int area, direction, next, requirement, requirement_data;

  public static List<ConnectionInfo> get(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<ConnectionInfo> info = new SqlStatement()
          .select(ConnectionInfo.class).where("area").eq(number).getList();

      return info;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getDirection() {return direction;} public void setDirection(int d) {direction = d;}
  public int getNext() {return next;} public void setNext(int n) {next = n;}
  public int getRequirement() {return requirement;} public void setRequirement(int r) {requirement = r;}
  public int getRequirement_data() {return requirement_data;} public void setRequirement_data(int v) {requirement_data = v;}
  //@format
}