package jpkmn.game.item;

import jpkmn.game.base.ItemInfo;
import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.*;

public class Item {
  public Item(int itemID) {
    _id = itemID;

    ItemInfo info = ItemInfo.getInfo(itemID);

    _name = info.getName();
    _value = info.getValue();
    _data = info.getData();

    _type = ItemType.valueOf(info.getType());
  }

  public String name() {
    return _name;
  }

  public int id() {
    return _id;
  }

  public ItemType type() {
    return _type;
  }

  public int value() {
    return _value;
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

  public boolean effect(Pokemon p) {
    if (_quantity < 1) return false;
    _quantity--;
    return _type.effect(p, _data);
  }

  public String toString() {
    return _id + "-" + _quantity;
  }

  private String _name;
  private ItemType _type;
  private int _id, _value, _quantity, _data;
}