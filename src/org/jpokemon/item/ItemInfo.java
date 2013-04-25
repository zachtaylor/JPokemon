package org.jpokemon.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jpokemon.JPokemonConstants;
import org.jpokemon.exception.ConfigurationException;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class ItemInfo {
  @PrimaryKey
  private int number;

  private String name;
  private int type, data, value;

  private static Map<Integer, ItemInfo> cache = new HashMap<Integer, ItemInfo>();

  public static ItemInfo getInfo(int number) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    if (number < 1)
      throw new ConfigurationException(number + " is outside item range");

    if (cache.get(number) == null) {
      try {
        List<ItemInfo> info = SqlStatement.select(ItemInfo.class)
            .where("number").eq(number).getList();

        if (!info.isEmpty())
          cache.put(number, info.get(0));
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getData() {return data;} public void setData(int d) {data = d;}
  public int getValue() {return value;} public void setValue(int v) {value = v;}
  public String getName() {return name;} public void setName(String s) {name = s;}
  //@format
}