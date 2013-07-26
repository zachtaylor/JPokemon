package org.jpokemon.pokemon.storage;

import java.util.Iterator;

import org.zachtaylor.jnodalxml.XmlException;
import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.myna.Myna;

/**
 * A representation of all the PokemonStorageUnits allocated to a Player. <br>
 * <br>
 * PokemonStorageBlock supports 1 unit of unique size, to be used for the party.
 * Other units have common size.
 */
public class PokemonStorageBlock implements Iterable<PokemonStorageUnit> {
  public static final String XML_NODE_NAME = "pokemonstorage";

  public static int boxsize = 20;

  public static int boxcount = 8;
  
  static {
    Myna.configure(PokemonStorageBlock.class, "org.jpokemon.player");
  }

  public PokemonStorageBlock() {
    _data = new PokemonStorageUnit[boxcount + 1];

    _data[0] = new PokemonStorageUnit();
    for (int i = 1; i <= boxcount; i++)
      _data[i] = new PokemonStorageUnit(boxsize);
  }

  public PokemonStorageUnit get(int box) {
    if (box < 0 || box > boxcount)
      throw new IllegalArgumentException("Invalid box number: " + box);

    return _data[box];
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    for (PokemonStorageUnit psu : _data) {
      node.addChild(psu.toXml());
    }

    return node;
  }

  public void loadXml(XmlNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XmlException("Cannot read node");

    int i = 0;
    for (XmlNode child : node.getChildren(PokemonStorageUnit.XML_NODE_NAME)) {
      get(i++).loadXml(child);
    }
  }

  @Override
  public Iterator<PokemonStorageUnit> iterator() {
    return new PokemonStorageBlockIterator();
  }

  private class PokemonStorageBlockIterator implements Iterator<PokemonStorageUnit> {
    @Override
    public boolean hasNext() {
      return index <= boxcount;
    }

    @Override
    public PokemonStorageUnit next() {
      return _data[index++];
    }

    @Override
    public void remove() {
      // No
    }

    private int index = 0;
  }

  private PokemonStorageUnit[] _data;
}