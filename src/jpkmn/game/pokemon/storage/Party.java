package jpkmn.game.pokemon.storage;

import java.util.Iterator;

import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.JPokemonConstants;

public class Party implements Iterable<Pokemon> {
  public Party(Trainer p) {
    _owner = p;
    _amount = 0;
    _data = new Pokemon[JPokemonConstants.PARTYSIZE];
  }

  public Trainer owner() {
    return _owner;
  }

  public int size() {
    return _amount;
  }

  public Pokemon get(int i) {
    if (i < 0 || i >= _amount) return null;

    return _data[i];
  }

  public boolean add(Pokemon p) {
    if (p == null || _amount == JPokemonConstants.PARTYSIZE || contains(p))
      return false;

    _data[_amount++] = p;
    p.owner(_owner);

    return true;
  }

  public boolean remove(Pokemon p) {
    return remove(indexOf(p));
  }

  public boolean remove(int index) {
    if (index < 0 || index >= _amount) return false;

    _data[index].owner(null);

    for (int i = index; i < _amount - 1; i++)
      _data[i] = _data[i + 1];

    _data[--_amount] = null;

    return true;
  }

  public boolean contains(Pokemon p) {
    return indexOf(p) != -1;
  }

  public int countAwake() {
    int awake = 0;

    for (int i = 0; i < _amount; i++)
      if (_data[i].condition.awake()) awake++;

    return awake;
  }

  public boolean swap(int p1, int p2) {
    if (p1 < 0 || p2 < 0 || p1 >= _amount || p2 >= _amount || p1 == p2)
      return false;

    Pokemon swap = _data[p1];
    _data[p1] = _data[p2];
    _data[p2] = swap;

    return true;
  }

  @Override
  public Iterator<Pokemon> iterator() {
    // @preformat
    // Crazy cool trick learned from Adam Mlodzinski at
    // http://stackoverflow.com/questions/5107158/how-to-pass-parameters-to-anonymous-class
    // @format

    return new Iterator<Pokemon>() {
      public Iterator<Pokemon> init(Party p) {
        _party = p;
        _index = 0;

        return this;
      }

      @Override
      public boolean hasNext() {
        return _index < _party._amount;
      }

      @Override
      public Pokemon next() {
        return _party.get(_index++);
      }

      @Override
      public void remove() {
        // Don't need this
      }

      private int _index;
      private Party _party;
    }.init(this);
  }

  private int indexOf(Pokemon p) {
    for (int i = 0; i < _amount; i++)
      if (_data[i].equals(p)) return i;

    return -1;
  }

  private int _amount;
  private Pokemon[] _data;
  private Trainer _owner;
}