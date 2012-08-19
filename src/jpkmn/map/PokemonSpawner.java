package jpkmn.map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;

public class PokemonSpawner {
  public PokemonSpawner() {
    _spawn = new ArrayList<Spawn>();
  }

  public void add(int number, int low, int high, int flex) {
    for (int i = 0; i < flex; i++)
      _spawn.add(new Spawn(number, low, high));
  }

  public Pokemon spawn() {
    if (_spawn.isEmpty()) return null;

    int index = (int) (Math.random() * _spawn.size());

    return _spawn.get(index).make();
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

  private List<Spawn> _spawn;
}