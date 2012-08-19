package jpkmn.map;

import java.util.HashMap;
import java.util.Map;

import jpkmn.game.pokemon.Pokemon;

public class Water {
  public Water() {
    _spawnMap = new HashMap<String, PokemonSpawner>();
  }

  /**
   * Adds a pokemon to the set that appear in this fishin spot.
   * 
   * @param num Pokemon number
   * @param flex Representation of how often the Pokemon appears
   * @param low Minimum level value
   * @param high Maximum level value
   * @param rod Fishin Pole used used to get this pokemon
   */
  public void add(int num, int flex, int low, int high, String rod) {
    if (_spawnMap.get(rod) == null) _spawnMap.put(rod, new PokemonSpawner());

    _spawnMap.get(rod).add(num, low, high, flex);
  }

  /**
   * Spawns a new pokemon
   * 
   * @param itemID Fishin pole used on the water
   * @return Pokemon spawned, null if that item didn't work.
   */
  public Pokemon spawn(String rod) {
    PokemonSpawner spawner = _spawnMap.get(rod);

    if (spawner == null) return null;
    return spawner.spawn();
  }

  private Map<String, PokemonSpawner> _spawnMap;
}