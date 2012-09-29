package jpkmn.map.spawner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.game.base.SpawnInfo;
import jpkmn.game.pokemon.Pokemon;

public class PokemonSpawner {
  public PokemonSpawner(List<SpawnInfo> info) {
    _spawnMap = new HashMap<String, List<Spawn>>();

    for (SpawnInfo si : info)
      add(si.getPokemon_number(), si.getMin_level(), si.getMax_level(),
          si.getFlex(), si.getTag());
  }

  /**
   * Reports whether the PokemonSpawner can spawn a Pokemon for the given tag
   * 
   * @param tag Tag to check
   * @return True if the spawner can create a Pokemon with the tag
   */
  public boolean hasTag(String tag) {
    return _spawnMap.get(tag) != null;
  }

  /**
   * Creates a new instance of a Pokemon, spawnable using the specified item.
   * If itemName=null, spawn using tall grass
   * 
   * @param itemName Item used, ie "oldrod"
   * @return New instance of Pokemon
   */
  public Pokemon spawn(String itemName) {
    if (_spawnMap.get(itemName) == null)
      return null;

    List<Spawn> spawner = _spawnMap.get(itemName);

    int index = (int) (Math.random() * spawner.size());

    return spawner.get(index).make();
  }

  /**
   * Adds a Pokemon blueprint to this PokemonSpawner.
   * 
   * @param number Pokemon number
   * @param low Minimum level value
   * @param high Maximum level value
   * @param flex Integer representation of the Pokemon's appearance rate, with
   *          respect to other species
   * @param itemName Tag used to spawn this pokemon, itemName=null specifies
   *          tall grass.
   */
  private void add(int number, int low, int high, int flex, String itemName) {
    if (_spawnMap.get(itemName) == null)
      _spawnMap.put(itemName, new ArrayList<Spawn>());

    List<Spawn> spawner = _spawnMap.get(itemName);

    for (int i = 0; i < flex; i++)
      spawner.add(new Spawn(number, low, high));
  }

  /**
   * A blueprint of a spawnable Pokemon in a given level range.
   * 
   * @author zach
   */
  private class Spawn {
    public Spawn(int number, int low, int high) {
      _low = low;
      _high = high;
      _number = number;
    }

    /**
     * Generates a new instance of the Pokemon in the level range
     * 
     * @return New instance of this Pokemon.
     */
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

  private Map<String, List<Spawn>> _spawnMap;
}