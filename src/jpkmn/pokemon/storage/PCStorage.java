package jpkmn.pokemon.storage;

import jpkmn.Constants;

public class PCStorage {
  public PCStorage() {
    _boxes = new StorageBox[Constants.BOXNUMBER];
  }

  public int getTotal() {
    int total = 0;

    for (int i = 0; i < _available; i++)
      total += _boxes[i].size();

    return total;
  }

  private int _available;
  private StorageBox[] _boxes;
}
