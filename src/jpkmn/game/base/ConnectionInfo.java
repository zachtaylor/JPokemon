package jpkmn.game.base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.map.AreaConnection;
import jpkmn.map.Direction;
import jpkmn.map.Requirement;
import jpkmn.map.RequirementType;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class ConnectionInfo {
  @PrimaryKey
  private int number;
  private int direction, next, requirement, value;

  public static Map<Direction, AreaConnection> getConnectionMap(int number) {
    Direction d;
    AreaConnection con;
    Requirement req = null;
    RequirementType reqType;

    Map<Direction, AreaConnection> map = new HashMap<Direction, AreaConnection>();

    List<ConnectionInfo> allInfo = doGet(number);

    if (allInfo.isEmpty()) return null;

    for (ConnectionInfo info : allInfo) {
      d = Direction.valueOf(info.getDirection());

      if (info.getRequirement() > -1) {
        reqType = RequirementType.valueOf(info.getRequirement());
        req = new Requirement(reqType, info.getValue());
      }

      con = new AreaConnection(info.getNext(), req);

      map.put(d, con);
    }

    return map;
  }

  private static List<ConnectionInfo> doGet(int number) {
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