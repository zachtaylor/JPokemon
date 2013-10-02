package org.jpokemon.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class Store {
  @PrimaryKey
  @AutoIncrement
  private int id = -1;
  private String name;
  private List<Inventory> _inventory = new ArrayList<Inventory>();

  public static Store get(int number) {
    DataConnectionManager.init(JPokemonServer.databasepath);
    Store store = null;

    try {
      List<Store> query = SqlStatement.select(Store.class).where("id").eq(number).getList();

      if (query.size() > 0) {
        store = query.get(0);
        store._inventory = Inventory.get(store.id);
      }
    }
    catch (DataConnectionException e) {
    }

    return store;
  }

  public void commit() {
    try {
      if (id == -1) {
        SqlStatement.insert(this).execute();
      }
      else {
        SqlStatement.update(this).where("id").eq(id).execute();
      }
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }
  }

  //@preformat
  public int getId() { return id; } public void setId(int i) { id = i; }
  public String getName() { return name; } public void setName(String n) { name = n; }
  //@format

  public boolean isEmpty() {
    return _inventory.isEmpty();
  }

  public List<Inventory> getInventory() {
    return Collections.unmodifiableList(_inventory);
  }
}