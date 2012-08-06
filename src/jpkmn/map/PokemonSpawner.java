package jpkmn.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.game.pokemon.Pokemon;

public class PokemonSpawner {
  public PokemonSpawner() {
    _tags = new HashMap<String, List<Spawn>>();
  }

  public void add(int number, int low, int high, String tag) {
    add(tag, new Spawn(number, low, high));
  }

  public Pokemon spawn(String... tags) {
    List<Spawn> cur, spawnable = new ArrayList<Spawn>();

    for (String tag : tags) {
      cur = _tags.get(tag);

      for (Spawn spawn : cur) {
        if (!spawnable.contains(spawn)) spawnable.add(spawn);
      }
    }

    int index = (int) (Math.random() * spawnable.size());

    return spawnable.get(index).make();
  }

  private void add(String tag, Spawn spawn) {
    if (_tags.get(tag) == null) _tags.put(tag, new ArrayList<Spawn>());

    _tags.get(tag).add(spawn);
  }

  private class Spawn {
    public Spawn(int number, int low, int high) {
      _low = low;
      _high = high;
      _number = number;
    }

    public Pokemon make() {
      int range = _high - _low;
      int level = _low + (int) (Math.random() * (range + 1));

      return new Pokemon(_number, level);
    }

    public boolean equals(Object o) {
      try {
        return _number == ((Spawn) o)._number;
      } catch (ClassCastException c) {
        return false;
      }
    }

    private int _low, _high, _number;
  }

  private Map<String, List<Spawn>> _tags;
}