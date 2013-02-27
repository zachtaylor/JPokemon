package org.jpokemon.pokedex;

import java.util.HashMap;
import java.util.Map;

import jpkmn.exceptions.LoadException;
import junit.framework.TestCase;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokedex.Pokedex;
import org.jpokemon.pokedex.PokedexStatus;

public class PokedexTest extends TestCase implements JPokemonConstants {
  public static int power = 5, range = 20;

  Pokedex dex;

  protected void setUp() {
    dex = new Pokedex();
  }

  public void testSaw() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * range) + 1;

      dex.saw(cur);

      assertTrue(PokedexStatus.SAW == dex.status(cur));
    }
  }

  public void testOwn() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * range + 1);

      dex.own(cur);

      assertTrue(PokedexStatus.OWN == dex.status(cur));
    }
  }

  public void testNoValue() {
    int n = (int) (Math.random() * range + 1);

    assertEquals(PokedexStatus.NONE, dex.status(n));
  }

  public void testCount() {
    assertEquals(0, dex.count());

    int n = (int) (Math.random() * range + 1);

    dex.saw(n);

    assertEquals(1, dex.count());
  }

  public void testOwnOverridesSaw() {
    int n = (int) (Math.random() * range + 1);

    dex.saw(n);
    dex.own(n);

    assertTrue(PokedexStatus.OWN == dex.status(n));
  }

  public void testSawDoesNotOverrideOwn() {
    int n = (int) (Math.random() * range + 1);

    dex.own(n);
    dex.saw(n);

    assertTrue(PokedexStatus.OWN == dex.status(n));
  }

  public void testMinimumValue() {
    try {
      dex.status(0);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }

    try {
      dex.saw(0);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }

    try {
      dex.own(0);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testLoad() {
    Map<Integer, PokedexStatus> data = new HashMap<Integer, PokedexStatus>();

    for (int i = 0; i < power; i++) {
      data.put((int) (Math.random() * range), PokedexStatus.SAW);
      data.put((int) (Math.random() * range), PokedexStatus.OWN);
    }

    String loadData = "DEX: ";

    for (int i = 0; i < range; i++) {
      if (data.get(i) != null)
        loadData += i + "-" + data.get(i).ordinal() + " ";
    }

    try {
      dex.load(loadData);
    } catch (LoadException e) {
      assertTrue(e.getMessage(), false);
      e.printStackTrace();
    }

    for (int i = 0; i < range; i++)
      if (data.get(i) != null)
        assertEquals(data.get(i), dex.status(i + 1));

    dex = new Pokedex();
    try {
      dex.load("DEX: \n");
      assertEquals(0, dex.count());
    } catch (LoadException e) {
      fail("Empty dex should be allowed");
    }
  }

  public void testLoadNonsense() {
    try {
      dex.load("foo bar baz\n");
      assertTrue("Load nonsense was allowed", false);
    } catch (Exception e) {
      assertTrue(e instanceof LoadException);
    }

    try {
      dex.load("DEX: DRTRAN!\n");
      assertTrue("Load nonsense was allowed", false);
    } catch (Exception e) {
      assertTrue(e instanceof LoadException);
    }

    try {
      dex.load("DEX: 3-\n");
      assertTrue("Load nonsense was allowed", false);
    } catch (Exception e) {
      assertTrue(e instanceof LoadException);
    }
  }
}