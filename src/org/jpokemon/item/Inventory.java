package org.jpokemon.item;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.AutoIncrement;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class Inventory {
  @PrimaryKey
  @AutoIncrement
  private int id = -1;
  private int store, item, price, purchaseprice, denomination, available;

  public static List<Inventory> get(int store) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    try {
      return SqlStatement.select(Inventory.class).where("store").eq(store).getList();
    }
    catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return new ArrayList<Inventory>();
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
    }
  }

  //@preformat
  public int getId() { return id; } public void setId(int i) { id = i; }
  public int getStore() {return store;} public void setStore(int s) {store = s;}
  public int getItem() {return item;} public void setItem(int i) {item = i;}
  public int getPrice() {return price;} public void setPrice(int p) {price = p;}
  public int getPurchaseprice() {return purchaseprice;} public void setPurchaseprice(int p) {purchaseprice = p;}
  public int getDenomination() {return denomination;} public void setDenomination(int d) {denomination = d;}
  public int getAvailable() {return available;} public void setAvailable(int a) {available = a;}
  //@format
}