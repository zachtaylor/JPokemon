package org.jpokemon.player;

import jpkmn.exceptions.LoadException;
import junit.framework.TestCase;

import org.jpokemon.trainer.Progress;

public class ProgressTest extends TestCase {
  private Progress progress;

  public void setUp() {
    progress = new Progress();
  }

  public void testStorage() {
    assertFalse(progress.get(7));

    progress.put(7);

    assertTrue(progress.get(7));
  }

  public void testBounds() {
    try {
      progress.put(0);
      fail("Out of bounds event");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testDuplicatePut() {
    progress.put(7);

    try {
      progress.put(7);
      fail("Duplicate put should fail");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testSaveAndLoad() {
    Progress other = new Progress();

    progress.put(3);
    progress.put(6);
    progress.put(7);

    try {
      other.loadJSON(progress.toJSON());
    } catch (LoadException e) {
      e.printStackTrace();
      fail("Should not cause LoadException");
    }

    assertTrue(other.get(3));
    assertTrue(other.get(6));
    assertTrue(other.get(7));
  }
}