package org.jpokemon.map.store;

import java.util.List;

import org.jpokemon.JPokemonConstants;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;

public class Inventory {
  private int number, item, price, purchaseprice, denomination, amount;

  public static List<Inventory> get(int number) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    try {
      return SqlStatement.select(Inventory.class).where("number").eq(number).getList();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public int getItem() {return item;} public void setItem(int i) {item = i;}
  public int getPrice() {return price;} public void setPrice(int p) {price = p;}
  public int getPurchaseprice() {return purchaseprice;} public void setPurchaseprice(int p) {purchaseprice = p;}
  public int getDenomination() {return denomination;} public void setDenomination(int d) {denomination = d;}
  public int getAmount() {return amount;} public void setAmount(int a) {amount = a;}
  //@format
}