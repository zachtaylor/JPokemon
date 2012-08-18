package jpkmn.map;

import java.util.HashMap;
import java.util.Map;

import jpkmn.game.pokemon.Pokemon;

public class Water {
  public Water() {
    _spawnMap = new HashMap<String, PokemonSpawner>();
  }

  public void add(int num, int flex, int low, int high, String s) {
    _spawnMap.put(s, new PokemonSpawner(num, low, high, flex));
  }

  public Pokemon spawn(String... tags) {
    return null;
  }

  private Map<String, PokemonSpawner> _spawnMap;
}