package jpkmn.map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.pokemon.Pokemon;

public class Water implements PokemonGenerator {
  public Water() {
    _species = new ArrayList<Integer>();
  }

  @Override
  public void species(int num, int flex) {
    for (int i = 0; i < flex; i++)
      _species.add(num);
  }

  @Override
  public Pokemon species() {
    int number = _species.get((int) (Math.random() * _species.size()));
    int level = _low + (int) (Math.random() * (_high - _low + 1));

    return new Pokemon(number, level);
  }

  @Override
  public void range(int low, int high) {
    _low = low;
    _high = high;
  }

  private int _low, _high;
  private List<Integer> _species;
}