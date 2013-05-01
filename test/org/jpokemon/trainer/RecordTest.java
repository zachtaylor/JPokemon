package org.jpokemon.trainer;

import junit.framework.TestCase;

public class RecordTest extends TestCase {
  private Record record;

  public void setUp() {
    record = new Record();
  }

  public void testStorage() {
    assertFalse(record.getEvent(7));

    record.putEvent(7);

    assertTrue(record.getEvent(7));
  }

  public void testBounds() {
    try {
      record.putEvent(0);
      fail("Out of bounds event");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testDuplicatePut() {
    record.putEvent(7);

    try {
      record.putEvent(7);
      fail("Duplicate put should fail");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }
}