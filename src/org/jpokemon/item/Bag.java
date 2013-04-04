package org.jpokemon.item;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

import com.zachtaylor.jnodalxml.XMLException;
import com.zachtaylor.jnodalxml.XMLNode;

public class Bag {
  public static final String XML_NODE_NAME = "bag";

  public Item get(int itemID) {
    Item item = _items.get(itemID);

    if (item == null)
      _items.put(itemID, item = new Item(itemID));

    return item;
  }

  public JSONArray toJSON() {
    JSONArray data = new JSONArray();

    for (Item item : _items.values())
      if (item.visible())
        data.put(item.toJSON());

    return data;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    _items = new HashMap<Integer, Item>();

    for (Map.Entry<Integer, Item> itemEntry : _items.entrySet()) {
      if (itemEntry.getValue().amount() == 0)
        continue;

      node.addChild(itemEntry.getValue().toXML());
    }

    return node;
  }

  public void loadXML(XMLNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XMLException("Cannot read node");

    for (XMLNode itemNode : node.getChildren(Item.XML_NODE_NAME)) {
      Item i = get(Integer.parseInt(itemNode.getAttribute("number")));
      i.amount(Integer.parseInt(itemNode.getAttribute("quantity")));
    }
  }

  private Map<Integer, Item> _items = new HashMap<Integer, Item>();
}