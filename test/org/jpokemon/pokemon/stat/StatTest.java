package org.jpokemon.pokemon.stat;

import junit.framework.TestCase;

public class StatTest extends TestCase {
  Stat stat;

  public void setUp() {
    stat = new Stat();
    stat.iv(0);
    stat.level(100);
    stat.base(100);
  }

  public void testBase() {
    assertEquals(310, stat.max());
    assertEquals(stat.max(), stat.cur());
  }

  public void testLevel() {
    stat.level(10);

    assertEquals(40, stat.max());
    assertEquals(stat.max(), stat.cur());
  }

  public void testPoints() {
    stat.points(100);

    assertEquals(100, stat.points());
    assertEquals(360, stat.max());
    assertEquals(stat.max(), stat.cur());
  }

  public void testModify() {
    stat.modify(1 / 2.0);

    assertEquals(155, stat.cur());
  }

  public void testEffectUpOne() {
    stat.effect(1);

    assertEquals(310, stat.max());
    assertEquals(465, stat.cur());
  }

  public void testEffectDownOne() {
    stat.effect(-1);

    assertEquals(310, stat.max());
    assertEquals(206, stat.cur());
  }

  public void testReset() {
    stat.effect(-6);
    assertEquals(310, stat.max());
    assertEquals(77, stat.cur());

    stat.reset();
    assertEquals(310, stat.max());
    assertEquals(310, stat.cur());
  }

  public void testResetDoesNotOverrideModify() {
    stat.effect(-6);
    assertEquals(77, stat.cur());

    stat.modify(1.0 / 2);
    assertEquals(38, stat.cur());

    stat.reset();
    assertEquals(155, stat.cur());
  }

  public void testModifyDoesNotOverrideEffect() {
    stat.effect(-6);
    stat.modify(1.0 / 2);

    assertEquals(38, stat.cur());

    stat.modify(1);

    assertEquals(77, stat.cur());
  }

  public void testEffectMaximum() {
    stat.effect(6);

    assertEquals(310, stat.max());
    assertEquals(1240, stat.cur());

    stat.effect(2);

    assertEquals(310, stat.max());
    assertEquals(1240, stat.cur());
  }

  public void testEffectMinimum() {
    stat.effect(-6);

    assertEquals(310, stat.max());
    assertEquals(77, stat.cur());

    stat.effect(-2);

    assertEquals(310, stat.max());
    assertEquals(77, stat.cur());
  }

  public void testMinimumValue() {
    stat.base(-1);
    stat.level(-1);
    stat.effect(-6);

    assertEquals(2, stat.cur());
  }

  public void testModifyAndEffect() {
    stat.modify(1.0 / 2);
    stat.effect(2);

    assertEquals(310, stat.cur());
  }

  public void testEV() {
    assertEquals(310, stat.max());

    stat.ev(100);
    stat.level(100);

    assertEquals(100, stat.ev());
    assertEquals(335, stat.max());
  }

  public void testIV() {
    stat.iv(100);

    assertEquals(100, stat.iv());
    assertEquals(410, stat.max());
  }
}