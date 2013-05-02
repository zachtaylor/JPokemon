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
    assertFalse(record.getTrainer(3));

    record.putTrainer(3);

    assertTrue(record.getTrainer(3));
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

  public void testTrainerBounds() {
    try {
      record.getTrainer(0);
      fail("Out of bounds trainer");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }

    try {
      record.putTrainer(0);
      fail("Out of bounds trainer");
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
    record.putTrainer(3);

    try {
      record.putTrainer(3);
      fail("Duplicate put should fail");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalArgumentException);
    }
  }
}