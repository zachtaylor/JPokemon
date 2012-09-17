package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public abstract class Item {
  public Target target;

  public Item(String name, int id, int value) {
    _name = name;
    _id = id;
    _value = value;
  }

  public String name() {
    return _name;
  }

  public int id() {
    return _id;
  }

  public void amount(int quantity) {
    _quantity = quantity;
  }

  public int amount() {
    return _quantity;
  }

  public int value() {
    return _value;
  }

  /**
   * Reduces the quantity
   * 
   * @return If quantity was greater than 0
   */
  boolean reduce() {
    return _quantity-- > 0;
  }

  public abstract boolean effect(Pokemon p);

  private String _name;
  private int _id, _value, _quantity;
}