package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class EventInfo {
  @PrimaryKey
  private int number;

  private int area, type, data1, data2, requirement, reqData;

  public static List<EventInfo> getEventsForArea(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<EventInfo> info = new SqlStatement().select(EventInfo.class)
          .where("area").eq(number).getList();

      return info;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getData1() {return data1;} public void setData1(int d) {data1 = d;}
  public int getData2() {return data2;} public void setData2(int d) {data2 = d;}
  public int getNumber() {return number;} public void setNumber(int _val) {number = _val;}
  public int getReqData() {return reqData;} public void setReqData(int rd) {reqData = rd;}
  public int getRequirement() {return requirement;} public void setRequirement(int r) {requirement = r;}
  //@format
}