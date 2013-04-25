package org.jpokemon.pokedex;

import junit.framework.TestCase;

public class PokedexTest extends TestCase {
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
}