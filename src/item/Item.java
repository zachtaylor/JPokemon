package item;

import battle.Target;
import pokemon.*;

public abstract class Item {
  public Target target;
  
  public Item(int power, int quantity, String name) {
    _power = power;
    _quantity = quantity;
    _name = name;
  }
  
  public void add(int quantity) {
    _quantity += quantity;
  }
  
  public String getName() {
    return _name;
  }
  
  public int getQuantity() {
    return _quantity;
  }
  
  public int getPower() {
    return _power;
  }
  
  public String name() {
    return _name;
  }
  
  public abstract boolean effect(Pokemon p);
  
  /**
   * Reduces quantity
   * @return True if quantity was at least 1
   */
  protected boolean reduce() {
    return _quantity-- > 0;
  }
  
  public String toString() {
    return _name + " "+_quantity;
  }

  private int _quantity;
  private final String _name;
  private final int _power;
}
