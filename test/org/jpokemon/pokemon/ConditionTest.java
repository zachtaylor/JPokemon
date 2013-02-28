package org.jpokemon.pokemon;

import junit.framework.TestCase;

import org.junit.Test;

public class ConditionTest extends TestCase {
  static Condition condition;

  public void setUp() throws Exception {
    condition = new Condition();
  }

  @Test
  public void testCatchBonusSingle() {
    ConditionEffect ce1 = ConditionEffect.BURN;

    condition.add(ce1);

    assertEquals(ce1.catchBonus(), condition.getCatchBonus());
  }

  public void testCatchBonusManyMax() {
    for (ConditionEffect ce : ConditionEffect.values())
      condition.add(ce);

    assertEquals(2.0, condition.getCatchBonus());
  }

  public void testPreventDuplicateEffects() {
    condition.add(ConditionEffect.SLEEP);
    condition.add(ConditionEffect.SLEEP);

    assertTrue(condition.contains(ConditionEffect.SLEEP));

    condition.remove(ConditionEffect.SLEEP);

    assertTrue(!condition.contains(ConditionEffect.SLEEP));
  }

  public void testCanAttack() {
    assertTrue("No condition effects", condition.canAttack());

    condition.add(ConditionEffect.POISON);

    assertTrue("Poison doesn't interfere", condition.canAttack());

    condition.add(ConditionEffect.FREEZE);
    
    assertTrue("Freeze guarantees no moving", !condition.canAttack());
  }

  public void testToString() {
    assertEquals("", condition.toString());

    condition.add(ConditionEffect.BURN);

    assertEquals("BURN", condition.toString());

    condition.add(ConditionEffect.SLEEP);

    assertEquals("[BURN, SLEEP]", condition.toString());
  }
}