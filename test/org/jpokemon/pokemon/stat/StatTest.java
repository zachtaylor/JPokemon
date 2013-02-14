package org.jpokemon.pokemon.stat;

import junit.framework.TestCase;

import org.jpokemon.pokemon.stat.Stat;
import org.junit.Test;

public class StatTest extends TestCase {
  Stat stat;

  public void setUp() {
    stat = new Stat();
    stat.iv(0);
  }

  @Test
  public void testBase() {
    stat.base(100);

    assertEquals(13, stat.cur());
  }

  public void testLevel() {
    stat.base(100);
    stat.level(100);

    assertEquals(310, stat.cur());
  }

  public void testPoints() {
    stat.base(100);
    stat.level(100);
    stat.points(100);

    assertEquals(360, stat.cur());
  }

  public void testModify() {
    stat.base(100);
    stat.level(100);
    stat.modify(1 / 2.0);

    assertEquals(155, stat.cur());
  }

  public void testEffectUpOne() {
    stat.base(100);
    stat.level(100);
    stat.effect(1);

    assertEquals(465, stat.cur());
  }

  public void testEffectDownOne() {
    stat.base(100);
    stat.level(100);
    stat.effect(-1);

    assertEquals(206, stat.cur());
  }

  public void testReset() {
    stat.base(100);
    stat.level(100);
    assertEquals(310, stat.cur());

    stat.effect(-6);
    assertEquals(77, stat.cur());

    stat.reset();
    assertEquals(310, stat.cur());
  }

  public void testResetDoesNotOverrideModify() {
    stat.base(100);
    stat.level(100);
    assertEquals(310, stat.cur());

    stat.effect(-6);
    assertEquals(77, stat.cur());

    stat.modify(1.0 / 2);
    assertEquals(38, stat.cur());

    stat.reset();
    assertEquals(155, stat.cur());
  }

  public void testModifyDoesNotOverrideEffect() {
    stat.base(100);
    stat.level(100);
    stat.effect(-6);
    stat.modify(1.0 / 2);

    assertEquals(38, stat.cur());

    stat.modify(1);

    assertEquals(77, stat.cur());
  }

  public void testEffectMaximum() {
    stat.base(100);
    stat.level(100);
    stat.effect(6);

    assertEquals(1240, stat.cur());
  }

  public void testEffectMinimum() {
    stat.base(100);
    stat.level(100);
    stat.effect(-8);

    assertEquals(77, stat.cur());
  }

  public void testMinimumValue() {
    stat.base(1);
    stat.level(1);
    stat.modify(1.0 / 100000);

    assertEquals(1, stat.cur());
  }

  public void testModifyAndEffect() {
    stat.base(100);
    stat.level(100);
    stat.modify(1.0 / 2);
    stat.effect(2);

    assertEquals(310, stat.cur());
  }
}