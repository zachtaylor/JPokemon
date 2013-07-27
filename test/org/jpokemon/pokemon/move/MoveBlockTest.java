package org.jpokemon.pokemon.move;

import junit.framework.TestCase;

import org.junit.Test;

public class MoveBlockTest extends TestCase {
  MoveBlock block;
  int pokemonNumber, moveRange = 40, pokemonRange = 25;

  public void setUp() {
    pokemonNumber = 1 + (int) (Math.random() * pokemonRange) + 1;
    block = new MoveBlock(pokemonNumber);
  }

  @Test
  public void testEveryPokemonHasDefaultMoves() {
    for (int i = 1; i <= pokemonRange; i++) {
      pokemonNumber = i;
      block = new MoveBlock(pokemonNumber);

      assertTrue(block.count() > 0);
    }
  }

  public void testAdd() {
    while (block.count() == MoveBlock.movecount)
      setUp();

    int count = block.count();

    try {
      block.add(0);
      fail("You cannot add Move #0");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }

    assertEquals(count, block.count());

    block.add(3);

    assertEquals(count + 1, block.count());

    int number;
    for (number = 14; block.count() < MoveBlock.movecount; number++) {
      try {
        block.add(number);
      } catch (IllegalArgumentException e) { // Duplicate move exception is ok
      }
    }

    try {
      for (; block.count() < MoveBlock.movecount; number++) {
        try {
          block.add(number);
        } catch (IllegalArgumentException e) { // Duplicate move exception is ok
        }
      }
      block.add(number);
      fail("Should die when overadding things");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
    }
  }

  public void testAddOverwrite() {
    try {
      block.add(3, -1);
      fail("Cannot add to position -1");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }

    int moveNumber;

    do {
      moveNumber = (int) (Math.random() * moveRange);
    } while (moveNumber == block.get(0).number());

    block.add(moveNumber, 0);

    assertEquals(new Move(moveNumber), block.get(0));

    while (block.count() >= MoveBlock.movecount - 1)
      setUp();

    int count = block.count();

    try {
      block.add(0, count);
      fail("You cannot add Move #0");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }

    assertEquals(count, block.count());

    try {
      block.add(count, count + 1);
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testRemoveAll() {
    block.removeAll();

    assertEquals(0, block.count());
  }

  public void testRestoreAll() {
    while (block.count() < 2)
      setUp();

    while (block.get(0).pp() > 0)
      block.get(0).use();
    while (block.get(1).pp() > 0)
      block.get(1).use();

    assertTrue(!block.get(0).enabled());
    assertTrue(!block.get(1).enabled());

    block.restoreAll();

    assertTrue(block.get(0).enabled());
    assertTrue(block.get(1).enabled());
  }

  public void testRandomizeNoMoves() {
    block.randomize(0);

    assertTrue(block.count() > 0);
  }

  public void testPreventDuplicateMoves() {
    int moveNumber = (int) (Math.random() * moveRange);

    block.removeAll();
    block.add(moveNumber);

    try {
      block.add(moveNumber);
      fail("Duplicate move allowed");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }
}