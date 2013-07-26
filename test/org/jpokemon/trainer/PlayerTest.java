package org.jpokemon.trainer;

import junit.framework.TestCase;

import org.jpokemon.pokedex.PokedexStatus;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;

public class PlayerTest extends TestCase {
  static int range = 25;

  Player player;

  public void setUp() {
    player = new Player("id");
  }

  public void testName() {
    String name = "hello world";
    player.name(name);

    assertEquals(name, player.name());
  }

  public void testArea() {
    int area = 80;
    player.area(area);

    assertEquals(area, player.area());
  }

  public void testBadge() {
    int badge = 2;
    player.badge(badge);

    assertEquals(badge, player.badge());
  }

  public void testCash() {
    int cash = 200;
    player.cash(cash);

    assertEquals(cash, player.cash());
  }

  public void testAddUsesStorage() {
    int i = 1;
    while (player.party().add(new Pokemon(i)))
      i++;

    assertEquals(PokemonStorageUnit.partysize, player.party().size());
    assertFalse(player.party().add(new Pokemon(i)));
    assertTrue(player.add(new Pokemon(i)));
  }

  public void testAddPokemonUpdatesPokedex() {
    int number = (int) (Math.random() * range) + 1;
    Pokemon p = new Pokemon(number);

    assertEquals(PokedexStatus.NONE, player.pokedex().status(number));

    player.add(p);

    assertEquals(PokedexStatus.OWN, player.pokedex().status(number));
  }
}