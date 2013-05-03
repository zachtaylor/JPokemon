package org.jpokemon.trainer;

import junit.framework.TestCase;

public class RecordTest extends TestCase {
  private Record record;

  public void setUp() {
    record = new Record();
  }

  public void testGetPutEvent() {
    assertFalse(record.getEvent(7));

    record.putEvent(7);

    assertTrue(record.getEvent(7));
  }

  public void testGetPutTrainer() {
    assertFalse(record.getTrainer("foo"));

    record.putTrainer("foo");

    assertTrue(record.getTrainer("foo"));
  }

  public void testEventBounds() {
    try {
      record.getEvent(0);
      fail("Out of bounds event");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }

    try {
      record.putEvent(0);
      fail("Out of bounds event");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testRepeatPutEvent() {
    record.putEvent(7);

    try {
      record.putEvent(7);
      fail("Duplicate put should fail");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }

  public void testRepeatPutTrainer() {
    record.putTrainer("gary");

    try {
      record.putTrainer("gary");
      fail("Duplicate put should fail");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }
}