package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public abstract class Item {
  public Target target;

  public Item(String name, int value) {
    _name = name;
  }

  public void amount(int quantity) {
    _quantity = quantity;
  }

  public int amount() {
    return _quantity;
  }

  public String name() {
    return _name;
  }

  public int value() {
    return _value;
  }

  public abstract boolean effect(Pokemon p);

  /**
   * Reduces quantity
   * 
   * @return True if quantity was at least 1
   */
  protected boolean reduce() {
    return _quantity-- > 0;
  }

  public String toString() {
    return _name + " " + _quantity;
  }

  private String _name;
  private int _value, _quantity;
}
