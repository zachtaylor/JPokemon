package org.jpokemon.pokemon;

import junit.framework.TestCase;

public class PokemonTest extends TestCase {
  static int number, level;
  static Pokemon pokemon;

  static int[] randomPokemon = { 1, 4, 7, 10, 13 };

  public void setUp() {
    number = randomPokemon[(int) (Math.random() * randomPokemon.length)];
    level = 5 + (int) (Math.random() * 5);

    pokemon = new Pokemon(number, level);
  }

  public void testName() {
    String nickname = "foo";

    assertEquals(pokemon.species(), pokemon.name());

    pokemon.name(nickname);

    assertEquals(nickname, pokemon.name());

    pokemon.name(null);

    assertEquals(pokemon.species(), pokemon.name());
  }

  public void testXp() {
    assertEquals(0, pokemon.xp());
    assertEquals(level, pokemon.level());

    pokemon.xp(1);

    assertEquals(1, pokemon.xp());
    assertEquals(level, pokemon.level());

    pokemon.xp(pokemon.xpNeeded());

    assertEquals(0, pokemon.xp());
    assertEquals(level + 1, pokemon.level());
  }

  public void testOriginalTrainer() {
    assertTrue(!pokemon.hasOriginalTrainer());
    assertEquals(null, pokemon.getTrainerName());

    pokemon.setTrainerName("foo");

    assertTrue(pokemon.hasOriginalTrainer());
    assertEquals("foo", pokemon.getTrainerName());

    pokemon.setTrainerName("bar");

    assertTrue(!pokemon.hasOriginalTrainer());
    assertEquals("foo", pokemon.getTrainerName());
  }

}