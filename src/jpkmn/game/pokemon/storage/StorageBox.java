package jpkmn.game.pokemon.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.JPokemonConstants;

public class StorageBox implements Iterable<Pokemon> {
  public StorageBox() {
    _size = 0;
    _storage = new ArrayList<Pokemon>();
  }

  public int size() {
    return _size;
  }

  public boolean add(Pokemon p) {
    if (_size == JPokemonConstants.BOXSIZE || _storage.contains(p)) return false;

    _size++;
    _storage.add(p);
    return true;
  }

  public boolean remove(Pokemon p) {
    if (_storage.remove(p)) {
      _size--;
      return true;
    }
    return false;
  }

  @Override
  public Iterator<Pokemon> iterator() {
    return _storage.iterator();
  }

  private int _size;
  private List<Pokemon> _storage;
}