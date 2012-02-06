package item;

import battle.Target;
import pokemon.*;

public abstract class Item {
  int power, quantity;
  String name;
  public Target target;
  
  public Item(int power, int quantity, String name) {
    this.power = power;
    this.quantity = quantity;
    this.name = name;
  }
  
  public Item(int power, int quantity) {
    this.power = power;
    this.quantity = quantity;
  }
  
  public void add(int quantity) {
    this.quantity += quantity;
  }
  
  public String getName() {
    return name;
  }
  
  public int amount() {
    return quantity;
  }
  
  public int power() {
    return power;
  }
  
  public String name() {
    return name;
  }
  
  public abstract boolean effect(Pokemon p);
  
  /**
   * Reduces quantity
   * @return True if quantity was at least 1
   */
  protected boolean reduce() {
    return quantity-- > 0;
  }
}
