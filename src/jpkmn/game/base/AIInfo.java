package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class AIInfo {
  @PrimaryKey
  private int number;

  private int area, cash, type, requirement, reqData;
  private String name;

  public static List<AIInfo> getAIForArea(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<AIInfo> info = new SqlStatement().select(AIInfo.class).where("area")
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
  public int getRequirement() {return requirement;} public void setRequirement(int r) {requirement = r;}
  public int getReqData() {return reqData;} public void setReqData(int rd) {reqData = rd;}
  public String getName() {return name;} public void setName(String _val) {name = _val;}
  //@format
}