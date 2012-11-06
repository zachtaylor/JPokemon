package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class SpawnInfo {
  private String tag;
  private int area, pokemon_number, min_level, max_level, flex;

  /**
   * Gets the list of spawn info from the database
   * 
   * @param number The area number
   * @return List of SpawnInfo from the database
   */
  public static List<SpawnInfo> get(int number) {
    DataConnectionManager.init("data/Pokemon.db");

    try {
      List<SpawnInfo> info = SqlStatement.select(SpawnInfo.class).where("area")
          .eq(number).getList();

      return info;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public String getTag() {return tag;} public void setTag(String t) {tag = t;}
  public int getPokemon_number() {return pokemon_number;} public void setPokemon_number(int p) {pokemon_number = p;}
  public int getMin_level() {return min_level;} public void setMin_level(int m) {min_level = m;}
  public int getMax_level() {return max_level;} public void setMax_level(int m) {max_level = m;}
  public int getFlex() {return flex;} public void setFlex(int f) {flex = f;}
  //@format
}