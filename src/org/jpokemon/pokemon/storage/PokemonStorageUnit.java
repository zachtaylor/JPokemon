package org.jpokemon.pokemon.storage;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import org.jpokemon.pokemon.Pokemon;
import org.zachtaylor.jnodalxml.XmlException;
import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.myna.Myna;

/**
 * A unit of storage for Pokemon.
 */
public class PokemonStorageUnit implements Iterable<Pokemon> {
  public static final String XML_NODE_NAME = "pokemonbox";

  public static int partysize = 6;

  static {
    Myna.configure(PokemonStorageUnit.class, "org.jpokemon.player");
  }

  public PokemonStorageUnit() {
    this(partysize);
  }

  public PokemonStorageUnit(int size) {
    _size = size;
    _amount = 0;
    _version = 0;
    _data = new Pokemon[_size];
  }

  public int size() {
    return _amount;
  }

  public Pokemon get(int i) {
    if (i < 0 || i >= _amount) throw new IllegalArgumentException("Index out of bounds: " + i);

    return _data[i];
  }

  public boolean add(Pokemon p) {
    if (p == null || _amount == _size || contains(p)) return false;

    _data[_amount++] = p;
    _version++;

    return true;
  }

  public boolean remove(Pokemon p) {
    int index = indexOf(p);

    if (index < 0) throw new IllegalArgumentException("Not in this unit: " + p);

    _version++;

    return remove(index);
  }

  public boolean slide(int indexOld, int indexNew) {
    if (indexOld < 0 || indexNew < 0 || indexOld >= _amount || indexNew >= _amount)
      throw new IllegalArgumentException("Index out of bounds");
    if (indexOld == indexNew)
      throw new IllegalArgumentException("Arguments are equal");

    Pokemon swap = _data[indexOld];
    int loopUpdateDirection = (indexOld > indexNew) ? -1 : 1;
    for (int i = indexOld; i != indexNew; i = i + loopUpdateDirection) {
      _data[i] = _data[i + loopUpdateDirection];
    }
    _data[indexNew] = swap;
    return true;
  }

  public boolean swap(int p1, int p2) {
    if (p1 < 0 || p2 < 0 || p1 >= _amount || p2 >= _amount) throw new IllegalArgumentException("Index out of bounds");
    if (p1 == p2) throw new IllegalArgumentException("Arguments are equal");

    Pokemon swap = _data[p1];
    _data[p1] = _data[p2];
    _data[p2] = swap;
    _version++;

    return true;
  }

  public int indexOf(Pokemon p) {
    for (int i = 0; i < _amount; i++) {
      if (_data[i].equals(p)) { return i; }
    }

    return -1;
  }

  public boolean contains(Pokemon p) {
    return indexOf(p) != -1;
  }

  public int awake() {
    int answer = 0;

    for (Pokemon pokemon : this) {
      if (pokemon.awake()) answer++;
    }

    return answer;
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    for (Pokemon p : this) {
      node.addChild(p.toXml());
    }

    return node;
  }

  public void loadXml(XmlNode node) {
    if (!XML_NODE_NAME.equals(node.getName())) throw new XmlException("Cannot read node");

    while (_amount > 0)
      remove(0);

    for (XmlNode child : node.getChildren(Pokemon.XML_NODE_NAME)) {
      Pokemon p = new Pokemon(1);
      p.loadXml(child);
      add(p);
    }
  }

  public Iterator<Pokemon> iterator() {
    return new PokemonStorageUnitIterator();
  }

  private boolean remove(int index) {
    if (index < 0 || index >= _amount) throw new IllegalArgumentException("Index out of bounds");

    for (int i = index; i < _amount - 1; i++)
      _data[i] = _data[i + 1];

    _data[--_amount] = null;

    return true;
  }

  private class PokemonStorageUnitIterator implements Iterator<Pokemon> {
    public PokemonStorageUnitIterator() {
      _index = 0;
      _version = PokemonStorageUnit.this._version;
    }

    @Override
    public boolean hasNext() {
      checkVersion();

      return _index < PokemonStorageUnit.this._amount;
    }

    @Override
    public Pokemon next() {
      checkVersion();

      return PokemonStorageUnit.this.get(_index++);
    }

    @Override
    public void remove() { // No
    }

    private void checkVersion() {
      if (_version != PokemonStorageUnit.this._version) throw new ConcurrentModificationException();
    }

    private int _index, _version;
  }

  private Pokemon[] _data;
  private int _size, _amount, _version;
}