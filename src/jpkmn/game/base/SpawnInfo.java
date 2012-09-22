package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class SpawnInfo {
  @PrimaryKey
  private int number;

  private String tag;
  private int pkmn, min, max, flex;

  /**
   * Gets the list of spawn info from the database
   * 
   * @param number The area number
   * @return List of SpawnInfo from the database
   */
  public static List<SpawnInfo> get(int number) {
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

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public String getTag() {return tag;} public void setTag(String t) {tag = t;}
  public int getPkmn() {return pkmn;} public void setPkmn(int p) {pkmn = p;}
  public int getMin() {return min;} public void setMin(int m) {min = m;}
  public int getMax() {return max;} public void setMax(int m) {max = m;}
  public int getFlex() {return flex;} public void setFlex(int f) {flex = f;}
  //@format
}