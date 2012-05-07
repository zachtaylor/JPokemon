package jpkmn.map;

import java.util.ArrayList;
import java.util.List;

import jpkmn.pokemon.Pokemon;


/**
 * Works the same as a Route, but Water can be applied to any area. Also, Water
 * does not extend Area, so it cannot exist on it's own.
 * 
 * @author Zach
 */
public class Water {

  /**
   * Adds a species of Pokemon to the list that appear in this Route.
   * 
   * @param num The Pokemon number
   * @param flex An integer which represents the popularity of the Pokemon.
   */
  public void addSpecies(int num, int flex) {
    for (int i = 0; i < flex; i++)
      _pokemon.add(num);
  }

  /**
   * Gets a list of all the available species numbers for this Water.
   * 
   * @return A list of integers
   */
  public List<Integer> getSpecies() {
    List<Integer> ret = new ArrayList<Integer>();

    for (Integer i : _pokemon) {
      if (!ret.contains(i)) ret.add(i);
    }

    return ret;
  }

  /**
   * Sets the level range for this route.
   * 
   * @param low Lowest level encounter.
   * @param high Highest level encounter.
   */
  public void setRange(int low, int high) {
    _low = low;
    _high = high;
  }

  /**
   * Generates a random Pokemon that appears in this Route.
   * 
   * @return A new Pokemon
   */
  public Pokemon getRandomPokemon() {
    int number = _pokemon.get((int) (Math.random() * _pokemon.size()));
    int level = _low + (int) (Math.random() * (_high - _low + 1));

    return new Pokemon(number, level);
  }

  private int _low, _high;
  private List<Integer> _pokemon;
}