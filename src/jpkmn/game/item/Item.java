package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Pokemon;

public class Item {
  public Item(int itemID) {
    _info = ItemInfo.getInfo(itemID);
    _type = ItemType.valueOf(_info.getType());
  }

  public String name() {
    return _info.getName();
  }

  public ItemType type() {
    return _type;
  }

  public int value() {
    return _info.getValue();
  }

  public Target target() {
    return _type.target();
  }

  public int amount() {
    return _quantity;
  }

  public void amount(int quantity) {
    _quantity = quantity;
  }

  public void add(int quantity) {
    amount(quantity + amount());
  }

  public boolean effect(Pokemon p) {
    if (_quantity < 1)
      return false;

    _quantity--;
    return _type.effect(p, _info.getData());
  }

  public String toString() {
    return _info.getNumber() + "-" + _quantity;
  }

  private int _quantity;
  private ItemInfo _info;
  private ItemType _type;
}