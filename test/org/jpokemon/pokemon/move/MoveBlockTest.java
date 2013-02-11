package org.jpokemon.pokemon.move;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class MoveBlockTest extends TestCase {
  static int moveRange = 40, pokemonRange = 25;

  MoveBlock block;
  int pokemonNumber;

  public void setUp() {
    pokemonNumber = 1 + (int) (Math.random() * pokemonRange);
    block = new MoveBlock(pokemonNumber);
  }

  @Test
  public void testDefaultMoves() {
    for (int i = 1; i <= pokemonRange; i++) {
      pokemonNumber = i;
      block = new MoveBlock(pokemonNumber);

      assertTrue(block.count() > 0);
    }
  }

  public void testAddOverwrite() {
    int moveNumber = (int) (Math.random() * moveRange);

    block.add(moveNumber, 0);

    assertEquals(new Move(moveNumber), block.get(0));
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

  public void testNewMoves() {
    List<String> newMoves = new ArrayList<String>();

    for (int i = 2; i < 100 && newMoves.isEmpty(); i++)
      newMoves = block.newMoves(i);

    assertTrue(!newMoves.isEmpty());
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

  public void testSetPokemonNumber() {
    List<String> newMoves = new ArrayList<String>();

    int level = 2;
    for (; level < 100 && newMoves.isEmpty(); level++)
      newMoves = block.newMoves(level);

    level--;
    assertEquals(newMoves, block.newMoves(level));

    int otherNumber = (pokemonNumber + 15) % pokemonRange;
    block.setPokemonNumber(otherNumber);
    List<String> otherMoves = block.newMoves(level);

    assertTrue("OK if pokemon " + pokemonNumber + " is related to "
        + otherNumber, !newMoves.equals(otherMoves));
  }
}