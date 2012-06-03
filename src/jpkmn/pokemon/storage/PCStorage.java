package jpkmn.pokemon.storage;

import java.util.Iterator;

import jpkmn.Constants;
import jpkmn.pokemon.Pokemon;

public class PCStorage implements Iterable<Pokemon> {
  private int a; // Flag to do work
  
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
    // TODO Figure this out
    return null;
  }

  private int _available;
  private StorageBox[] _boxes;
}
