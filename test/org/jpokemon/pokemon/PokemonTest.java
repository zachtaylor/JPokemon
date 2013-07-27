package org.jpokemon.pokemon;

import junit.framework.TestCase;

import org.jpokemon.pokemon.stat.StatBlock;
import org.jpokemon.pokemon.stat.StatType;

public class PokemonTest extends TestCase {
  static int number, level;
  static Pokemon pokemon;

  static int[] randomPokemon = { 1, 4, 7, 10, 13 };

  public void setUp() {
    number = randomPokemon[(int) (Math.random() * randomPokemon.length)];
    level = 5 + (int) (Math.random() * 5);

    pokemon = new Pokemon(number, level);
  }

  public void testNumber() {
    assertEquals(number, pokemon.number());
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

  public void testStatLookup() {

    assertEquals(pokemon.attack(), pokemon.getStat(StatType.ATTACK).cur());
    assertEquals(pokemon.specattack(), pokemon.getStat(StatType.SPECATTACK).cur());
    assertEquals(pokemon.defense(), pokemon.getStat(StatType.DEFENSE).cur());
    assertEquals(pokemon.specdefense(), pokemon.getStat(StatType.SPECDEFENSE).cur());
    assertEquals(pokemon.speed(), pokemon.getStat(StatType.SPEED).cur());
  }

  public void testStatPoints() {
    try {
      pokemon.statPoints(StatType.ATTACK, 1);
      fail("Cannot spend when have no stat points to spend");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
    }

    assertEquals(0, pokemon.getStat(StatType.ATTACK).points());
    StatBlock.bonuslevelrate = 1.0;

    level++;
    pokemon.level(level);
    pokemon.statPoints(StatType.ATTACK, 1);
    assertEquals(1.0, pokemon.getStat(StatType.ATTACK).points());

    try {
      pokemon.statPoints(StatType.ATTACK, 1);
      fail("Cannot spend when have no stat points to spend");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
    }

    assertEquals((int) StatBlock.bonuslevelrate, pokemon.getStat(StatType.ATTACK).points());
  }

  public void testDamageAndHealth() {
    int maxhealth = pokemon.maxHealth();
    int damage = pokemon.health() - 1;

    assertTrue(pokemon.awake());

    pokemon.takeDamage(damage);
    assertEquals(1, pokemon.health());
    assertEquals(maxhealth, pokemon.maxHealth());
    assertTrue(pokemon.awake());

    pokemon.takeDamage(1);
    assertEquals(0, pokemon.health());
    assertEquals(maxhealth, pokemon.maxHealth());
    assertFalse(pokemon.awake());

    pokemon.healDamage(5);
    assertEquals(5, pokemon.health());
    assertEquals(maxhealth, pokemon.maxHealth());
    assertTrue(pokemon.awake());
  }

  public void testOriginalTrainer() {
    assertFalse(pokemon.hasOriginalTrainer());
    assertEquals(null, pokemon.getTrainerName());

    pokemon.setTrainerName("foo");

    assertTrue(pokemon.hasOriginalTrainer());
    assertEquals("foo", pokemon.getTrainerName());

    pokemon.setTrainerName("bar");

    assertTrue(!pokemon.hasOriginalTrainer());
    assertEquals("foo", pokemon.getTrainerName());
  }

}