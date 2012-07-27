package jpkmn.map;

import jpkmn.game.pokemon.Pokemon;

public class Water {
  public Water() {
    _spawner = new PokemonSpawner();
  }

  public void add(int num, int flex, int low, int high, String tag) {
    for (int i = 0; i < flex; i++)
      _spawner.add(num, low, high, tag);
  }

  public Pokemon spawn(String... tags) {
    return _spawner.spawn(tags);
  }

  private PokemonSpawner _spawner;
}