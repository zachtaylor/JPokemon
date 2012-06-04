package jpkmn.game.pokemon.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jpkmn.Constants;
import jpkmn.game.pokemon.Pokemon;

public class PCStorage implements Iterable<Pokemon> {
  public PCStorage() {
    _boxes = new StorageBox[Constants.BOXNUMBER];
    addBox();
  }

  public int getTotal() {
    int total = 0;

    for (int i = 0; i < _available; i++)
      total += _boxes[i].size();

    return total;
  }

  public void addBox() {
    if (_available < Constants.BOXNUMBER)
      _boxes[_available++] = new StorageBox();

  }

  public boolean add(Pokemon p) {
    for (int i = 0; i < _available; i++) {
      if (_boxes[i].add(p)) return true;
    }
    return false;
  }

  @Override
  public Iterator<Pokemon> iterator() {
    return createPCStorageIterator(this);
  }

  private Iterator<Pokemon> createPCStorageIterator(PCStorage pcs) {
    List<Pokemon> pokemon = new ArrayList<Pokemon>();

    for (int box = 0; box < pcs._available; box++) {
      for (Pokemon p : _boxes[box]) {
        pokemon.add(p);
      }
    }

    return pokemon.iterator();
  }

  private int _available;
  private StorageBox[] _boxes;
}
