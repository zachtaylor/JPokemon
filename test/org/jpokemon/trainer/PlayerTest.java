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

  public void testBadge() {
    int badge = 2;
    player.setBadgeCount(badge);

    assertEquals(badge, player.getBadgeCount());
  }

  public void testCash() {
    int cash = 200;
    player.setCash(cash);

    assertEquals(cash, player.getCash());
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