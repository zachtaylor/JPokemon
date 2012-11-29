package org.jpokemon.pokemon.stat;

import junit.framework.TestCase;

import org.jpokemon.pokemon.stat.Stat;
import org.junit.Test;

public class StatTest extends TestCase {
  Stat stat;

  public void setUp() {
    stat = new Stat();
  }

  @Test
  public void testBase() {
    stat.base(100);

    assertEquals(7, stat.cur());
  }

  public void testLevel() {
    stat.base(100);
    stat.level(100);

    assertEquals(205, stat.cur());
  }

  public void testPoints() {
    stat.base(100);
    stat.level(100);
    stat.points(100);

    assertEquals(305, stat.cur());
  }

  public void testModify() {
    stat.base(100);
    stat.level(100);
    stat.modify(1 / 2.0);

    assertEquals(102, stat.cur());
  }

  public void testEffectUpOne() {
    stat.base(100);
    stat.level(100);
    stat.effect(1);

    assertEquals(307, stat.cur());
  }

  public void testEffectDownOne() {
    stat.base(100);
    stat.level(100);
    stat.effect(-1);

    assertEquals(136, stat.cur());
  }

  public void testReset() {
    stat.base(100);
    stat.level(100);
    stat.effect(-6);

    assertEquals(51, stat.cur());

    stat.reset();

    assertEquals(205, stat.cur());
  }

  public void testResetDoesNotOverrideModify() {
    stat.base(100);
    stat.level(100);
    stat.effect(-6);
    stat.modify(1.0 / 2);

    assertEquals(25, stat.cur());

    stat.reset();

    assertEquals(102, stat.cur());
  }

  public void testModifyDoesNotOverrideEffect() {
    stat.base(100);
    stat.level(100);
    stat.effect(-6);
    stat.modify(1.0 / 2);

    assertEquals(25, stat.cur());

    stat.modify(1);

    assertEquals(51, stat.cur());
  }

  public void testEffectMaximum() {
    stat.base(100);
    stat.level(100);
    stat.effect(6);

    assertEquals(820, stat.cur());
  }

  public void testEffectMinimum() {
    stat.base(100);
    stat.level(100);
    stat.effect(-8);

    assertEquals(51, stat.cur());
  }

  public void testModifyAndEffect() {
    stat.base(100);
    stat.level(100);
    stat.modify(1.0 / 2);
    stat.effect(2);

    assertEquals(205, stat.cur());
  }
}