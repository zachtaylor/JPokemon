package jpkmn.game.pokemon.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jpkmn.Constants;
import jpkmn.game.pokemon.Pokemon;

public class StorageBox implements Iterable<Pokemon> {
  public StorageBox() {
    _size = 0;
    storage = new ArrayList<Pokemon>();
  }

  public int size() {
    return _size;
  }

  public boolean add(Pokemon p) {
    if (_size == Constants.BOXSIZE || storage.contains(p)) return false;

    _size++;
    storage.add(p);
    return true;
  }

  public boolean remove(Pokemon p) {
    if (storage.remove(p)) {
      _size--;
      return true;
    }
    return false;
  }

  @Override
  public Iterator<Pokemon> iterator() {
    return storage.iterator();
  }

  private int _size;
  private List<Pokemon> storage;
}
