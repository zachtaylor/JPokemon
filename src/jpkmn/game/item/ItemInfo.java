package jpkmn.game.item;

import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.exception.ConfigurationException;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class ItemInfo implements JPokemonConstants {
  @PrimaryKey
  private int number;

  private String name;
  private int type, data, value;

  private static ItemInfo[] cache = new ItemInfo[ITEMNUMBER];

  public static ItemInfo getInfo(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    if (number < 1 || number > ITEMNUMBER)
      throw new ConfigurationException(number + " is outside item range");

    if (cache[number - 1] == null) {
      try {
        List<ItemInfo> info = SqlStatement.select(ItemInfo.class)
            .where("number").eq(number).getList();

        if (!info.isEmpty())
          cache[number - 1] = info.get(0);
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache[number - 1];
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getData() {return data;} public void setData(int d) {data = d;}
  public int getValue() {return value;} public void setValue(int v) {value = v;}
  public String getName() {return name;} public void setName(String s) {name = s;}
  //@format
}