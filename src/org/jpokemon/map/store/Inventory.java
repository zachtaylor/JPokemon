package org.jpokemon.map.store;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class Inventory {
  private int store, item, price, purchaseprice, denomination, available;

  public static List<Inventory> get(int store) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(Inventory.class).where("store").eq(store).getList();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getStore() {return store;} public void setStore(int s) {store = s;}
  public int getItem() {return item;} public void setItem(int i) {item = i;}
  public int getPrice() {return price;} public void setPrice(int p) {price = p;}
  public int getPurchaseprice() {return purchaseprice;} public void setPurchaseprice(int p) {purchaseprice = p;}
  public int getDenomination() {return denomination;} public void setDenomination(int d) {denomination = d;}
  public int getAvailable() {return available;} public void setAvailable(int a) {available = a;}
  //@format
}