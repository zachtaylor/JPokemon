package org.jpokemon.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zachtaylor.jnodalxml.XMLException;
import org.zachtaylor.jnodalxml.XMLNode;

public class Bag implements Iterable<Item> {
  public static final String XML_NODE_NAME = "bag";

  public Item get(int itemID) {
    Item item = _items.get(itemID);

    if (item == null)
      _items.put(itemID, item = new Item(itemID));

    return item;
  }

  @Override
  public Iterator<Item> iterator() {
    return new BagIterator();
  }

  public JSONArray toJSON() {
    JSONArray data = new JSONArray();

    Map<ItemType, JSONArray> itemTypes = new HashMap<ItemType, JSONArray>();

    for (Item item : _items.values()) {
      if (!item.visible())
        continue;

      if (itemTypes.get(item.type()) == null)
        itemTypes.put(item.type(), new JSONArray());

      itemTypes.get(item.type()).put(item.toJSON());
    }

    try {
      for (Map.Entry<ItemType, JSONArray> itemTypeEntry : itemTypes.entrySet()) {
        JSONObject itemType = new JSONObject();
        itemType.put("type", itemTypeEntry.getKey().toString().toLowerCase());
        itemType.put("items", itemTypeEntry.getValue());
        data.put(itemType);
      }
    } catch (JSONException e) {
    }

    return data;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    for (Map.Entry<Integer, Item> itemEntry : _items.entrySet()) {
      node.addChild(itemEntry.getValue().toXML());
    }

    return node;
  }

  public void loadXML(XMLNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XMLException("Cannot read node");

    for (XMLNode itemNode : node.getChildren(Item.XML_NODE_NAME)) {
      Item i = get(itemNode.getIntAttribute("number"));
      i.amount(itemNode.getIntAttribute("quantity"));
    }
  }

  private class BagIterator implements Iterator<Item> {
    @Override
    public boolean hasNext() {
      return index < keys.length;
    }

    @Override
    public Item next() {
      return Bag.this.get(keys[index++]);
    }

    @Override
    public void remove() { // nope
    }

    // One-liners, man... Fuck constructors.
    private int index = 0;
    private Integer[] keys = Bag.this._items.keySet().toArray(new Integer[Bag.this._items.keySet().size()]);
  }

  private Map<Integer, Item> _items = new HashMap<Integer, Item>();
}