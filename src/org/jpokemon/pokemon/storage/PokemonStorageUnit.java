package org.jpokemon.pokemon.storage;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.Pokemon;

/**
 * A unit of storage for Pokemon.
 */
public class PokemonStorageUnit implements Iterable<Pokemon> {
  public PokemonStorageUnit(int size) {
    _size = size;
    _amount = 0;
    _version = 0;
    _data = new Pokemon[_size];
  }

  public PokemonStorageUnit(int size, Trainer trainer) {
    this(size);
    _trainer = trainer;
  }

  public Trainer trainer() {
    return _trainer;
  }

  public int size() {
    return _amount;
  }

  public Pokemon get(int i) {
    if (i < 0 || i >= _amount)
      throw new IllegalArgumentException("Index out of bounds");

    return _data[i];
  }

  public boolean add(Pokemon p) {
    if (p == null || _amount == _size || contains(p))
      return false;

    _data[_amount++] = p;
    p.owner(_trainer);
    _version++;

    return true;
  }

  public boolean remove(Pokemon p) {
    int index = indexOf(p);

    if (index < 0)
      throw new IllegalArgumentException("Not in this unit: " + p);

    p.owner(null);
    return remove(index);
  }

  public boolean swap(int p1, int p2) {
    if (p1 < 0 || p2 < 0 || p1 >= _amount || p2 >= _amount)
      throw new IllegalArgumentException("Index out of bounds");
    if (p1 == p2)
      throw new IllegalArgumentException("Arguments are equal");

    Pokemon swap = _data[p1];
    _data[p1] = _data[p2];
    _data[p2] = swap;

    return true;
  }

  public boolean contains(Pokemon p) {
    return indexOf(p) != -1;
  }

  public Iterator<Pokemon> iterator() {
    return new PokemonStorageUnitIterator();
  }

  private int indexOf(Pokemon p) {
    for (int i = 0; i < _amount; i++)
      if (_data[i].equals(p))
        return i;
    return -1;
  }

  private boolean remove(int index) {
    if (index < 0 || index >= _amount)
      throw new IllegalArgumentException("Index out of bounds");

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

      return _index < PokemonStorageUnit.this._amount - 1;
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
      if (_version != PokemonStorageUnit.this._version)
        throw new ConcurrentModificationException();
    }

    private int _index, _version;
  }

  private Pokemon[] _data;
  private Trainer _trainer;
  private int _size, _amount, _version;
}