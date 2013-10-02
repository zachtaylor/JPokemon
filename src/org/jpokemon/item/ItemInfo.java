package org.jpokemon.item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class ItemInfo {
  @PrimaryKey
  private int id;

  private String name;
  private int type, data;

  private static Map<Integer, ItemInfo> cache = new HashMap<Integer, ItemInfo>();

  public static ItemInfo get(int number) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    if (cache.get(number) == null) {
      try {
        List<ItemInfo> info = SqlStatement.select(ItemInfo.class).where("id").eq(number).getList();

        if (!info.isEmpty()) cache.put(number, info.get(0));
      }
      catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }

  //@preformat
  public int getId() { return id; } public void setId(int i) { id = i; }
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getData() {return data;} public void setData(int d) {data = d;}
  public String getName() {return name;} public void setName(String s) {name = s;}
  //@format
}