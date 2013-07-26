package org.jpokemon.item;

import org.jpokemon.battle.Target;
import org.jpokemon.pokemon.Pokemon;
import org.zachtaylor.jnodalxml.XmlNode;

public class Item {
  public static final String XML_NODE_NAME = "item";

  public Item(int itemID) {
    _info = ItemInfo.get(itemID);
    _type = ItemType.valueOf(_info.getType());
  }

  public int number() {
    return _info.getNumber();
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

  public boolean visible() {
    return amount() > 0;
  }

  public boolean effect(Pokemon p) {
    if (_quantity < 1)
      return false;

    boolean result = _type.effect(p, _info);
    if (result)
      --_quantity;

    return result;
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    node.setAttribute("number", _info.getNumber());
    node.setAttribute("quantity", _quantity);
    node.setSelfClosing(true);

    return node;
  }

  public String toString() {
    return _info.getNumber() + "-" + _quantity;
  }

  private int _quantity;
  private ItemInfo _info;
  private ItemType _type;
}