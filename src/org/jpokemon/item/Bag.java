package org.jpokemon.item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.zachtaylor.jnodalxml.XmlException;
import org.zachtaylor.jnodalxml.XmlNode;

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

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    for (Map.Entry<Integer, Item> itemEntry : _items.entrySet()) {
      node.addChild(itemEntry.getValue().toXml());
    }

    return node;
  }

  public void loadXml(XmlNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XmlException("Cannot read node");

    for (XmlNode itemNode : node.getChildren(Item.XML_NODE_NAME)) {
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