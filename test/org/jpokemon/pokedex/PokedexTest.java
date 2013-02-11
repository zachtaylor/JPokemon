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

  public void testOwnOverridesSaw() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * range + 1);

      dex.saw(cur);
      dex.own(cur);

      assertTrue(PokedexStatus.OWN == dex.status(cur));
    }
  }

  public void testSawDoesNotOverrideOwn() {
    int cur;

    for (int i = 0; i < power; i++) {
      cur = (int) (Math.random() * range + 1);

      dex.own(cur);
      dex.saw(cur);

      assertTrue(PokedexStatus.OWN == dex.status(cur));
    }
  }

  public void testMinimumValue() {
    try {
      dex.status(0);
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

    loadData += "\n";

    try {
      dex.load(loadData);
    } catch (LoadException e) {
      assertTrue(e.getMessage(), false);
      e.printStackTrace();
    }

    for (int i = 0; i < range; i++)
      if (data.get(i) != null) assertEquals(data.get(i), dex.status(i + 1));
  }

  public void testLoadNonsense() {
    try {
      dex.load("foo bar baz\n");
      assertTrue("Load nonsense was allowed", false);
    } catch (Exception e) {
      assertTrue(e instanceof LoadException);
    }
  }
}