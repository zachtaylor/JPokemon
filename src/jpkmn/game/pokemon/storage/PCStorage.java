package jpkmn.game.pokemon.storage;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.JPokemonConstants;

public class PCStorage implements Iterable<Pokemon> {
  public PCStorage() {
    _boxes = new StorageBox[JPokemonConstants.BOXNUMBER];
    addBox();
  }

  public int total() {
    int total = 0;

    for (int i = 0; i < _available; i++)
      total += _boxes[i].size();

    return total;
  }

  public void addBox() {
    if (_available < JPokemonConstants.BOXNUMBER)
      _boxes[_available++] = new StorageBox();
  }

  public boolean add(Pokemon p) {
    for (int boxIndex = 0; boxIndex < _available; boxIndex++)
      if (_boxes[boxIndex].add(p)) return true;

    return false;
  }

  @Override
  public Iterator<Pokemon> iterator() {
    List<Pokemon> pokemon = new ArrayList<Pokemon>();

    for (int box = 0; box < _available; box++)
      for (Pokemon p : _boxes[box])
        pokemon.add(p);

    return pokemon.iterator();
  }

  private int _available;
  private StorageBox[] _boxes;
}