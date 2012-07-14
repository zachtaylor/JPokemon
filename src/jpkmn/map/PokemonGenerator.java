package jpkmn.map;

import jpkmn.game.pokemon.Pokemon;

public interface PokemonGenerator {
  /**
   * Generates a random Pokemon
   * 
   * @return A new Pokemon
   */
  public Pokemon species();

  /**
   * Adds a species of Pokemon to the list that appear
   * 
   * @param num The Pokemon number
   * @param flex An integer which represents the popularity of the Pokemon.
   */
  public void species(int num, int flex);

  /**
   * Sets the level range for this route.
   * 
   * @param low Lowest level encounter.
   * @param high Highest level encounter.
   */
  public void range(int low, int high);
}
