package org.jpokemon.item;

import org.jpokemon.battle.Target;
import org.jpokemon.pokemon.Pokemon;
import org.json.JSONException;
import org.json.JSONObject;

import com.zachtaylor.jnodalxml.XMLNode;

public class Item {
  public static final String XML_NODE_NAME = "item";

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

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("id", _info.getNumber());
      data.put("name", name());
      data.put("amount", amount());

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    node.setAttribute("number", _info.getNumber() + "");
    node.setAttribute("quantity", _quantity + "");
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