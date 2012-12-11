package org.jpokemon.pokemon.storage;

import jpkmn.exceptions.LoadException;
import jpkmn.game.pokemon.Pokemon;
import junit.framework.TestCase;

public class PokemonStorageUnitTest extends TestCase {
  private static int size = 6;

  public static PokemonStorageUnit unit;

  public void setUp() {
    unit = new PokemonStorageUnit(size);
  }

  public void testAdd() throws LoadException {
    unit.add(new Pokemon(1));

    assertEquals(1, unit.size());
  }

  public void testRemove() throws LoadException {
    Pokemon p = new Pokemon(1);
    unit.add(p);
    unit.remove(p);

    assertEquals(0, unit.size());
  }

  public void testGet() throws LoadException {
    Pokemon p = new Pokemon(1);
    unit.add(p);

    assertEquals(p, unit.get(0));
  }

  public void testSwap() throws LoadException {
    Pokemon p1 = new Pokemon(1);
    Pokemon p2 = new Pokemon(2);
    unit.add(p1);
    unit.add(p2);

    unit.swap(0, 1);

    assertEquals(p1, unit.get(1));
    assertEquals(p2, unit.get(0));
  }

  public void testAddLimit() throws LoadException {
    for (int i = 0; i < size + 1; i++) {
      unit.add(new Pokemon(i + 1));
    }

    assertEquals(size, unit.size());
  }

  public void testGetBounds() {
    try {
      unit.get(0);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testSwapMany() throws LoadException {
    Pokemon p1 = new Pokemon(1);
    Pokemon p2 = new Pokemon(2);
    Pokemon p3 = new Pokemon(3);
    Pokemon p4 = new Pokemon(4);
    unit.add(p1);
    unit.add(p2);
    unit.add(p3);
    unit.add(p4);

    unit.swap(0, 3);

    assertEquals(p4, unit.get(0));
    assertEquals(p2, unit.get(1));
    assertEquals(p3, unit.get(2));
    assertEquals(p1, unit.get(3));
  }

  public void testSwapBounds() {
    try {
      unit.swap(0, 1);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testSwapSame() throws LoadException {
    Pokemon p1 = new Pokemon(1);
    unit.add(p1);

    try {
      unit.swap(0, 0);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testRemoveNotInPartyException() throws LoadException {
    PokemonStorageUnit unit1 = new PokemonStorageUnit(6);
    
    Pokemon p1 = new Pokemon(1);
    Pokemon p2 = new Pokemon(2);
    
    unit1.add(p1);
    unit.add(p2);

    try {
      unit.remove(p1);
      fail();
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }
}