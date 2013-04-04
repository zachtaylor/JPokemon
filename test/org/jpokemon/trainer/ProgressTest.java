package org.jpokemon.trainer;

import junit.framework.TestCase;

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
}