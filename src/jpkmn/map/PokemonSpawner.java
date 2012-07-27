package jpkmn.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.game.pokemon.Pokemon;

public class PokemonSpawner {
  public PokemonSpawner() {
    tags = new HashMap<String, List<Spawn>>();
  }

  public void add(int number, int low, int high, String tag) {
    add(tag, new Spawn(number, low, high));
  }

  public Pokemon spawn(String... tags) {
    // TODO
    return null;
  }

  private void add(String tag, Spawn spawn) {
    if (tags.get(tag) == null) tags.put(tag, new ArrayList<Spawn>());

    tags.get(tag).add(spawn);
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

    private int _low, _high, _number;
  }

  private Map<String, List<Spawn>> tags;
}