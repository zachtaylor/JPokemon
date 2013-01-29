package org.jpokemon.trainer;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.AreaRegistry;
import junit.framework.TestCase;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokedex.PokedexStatus;
import org.jpokemon.trainer.Player;

public class PlayerTest extends TestCase implements JPokemonConstants {
  private static Player player;

  public void setUp() {
    player = new Player();
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

    assertEquals(TRAINER_PARTY_SIZE, player.party().size());
    assertFalse(player.party().add(new Pokemon(i)));
    assertTrue(player.add(new Pokemon(i)));
  }

  public void testAddPokemonUpdatesPokedex() {
    int number = (int) (Math.random() * POKEMONNUMBER + 1);
    Pokemon p = new Pokemon(number);

    assertEquals(PokedexStatus.NONE, player.pokedex().status(number));

    player.add(p);

    assertEquals(PokedexStatus.OWN, player.pokedex().status(number));
  }

  public void testNewPlayerHasValidArea() {
    int area = player.area();

    try {
      AreaRegistry.get(area);
    } catch (ArrayIndexOutOfBoundsException e) {
      fail();
    }
  }

  public void testPlayerAddAppliesOriginalTrainer() {
	Pokemon p = new Pokemon(1);
	
	assertEquals(null, p.originalTrainer());
	
	player.name("zach");
	player.add(p);
	
	assertEquals(player.name(), p.originalTrainer());
  }
}