package org.jpokemon.pokemon.stat;

import junit.framework.TestCase;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.ConditionEffect;
import org.jpokemon.pokemon.PokemonInfo;

public class StatBlockTest extends TestCase {
  StatBlock block;
  PokemonInfo pokemon;

  public void setUp() {
    int pokemonNumber = (int) (Math.random() * JPokemonConstants.POKEMONNUMBER);
    pokemon = PokemonInfo.get(pokemonNumber);
    block = new StatBlock(pokemon);
    block.level(100);
  }

  public void testGet() {
    Stat attackStat = block.get(StatType.ATTACK);

    assertEquals(pokemon.getAttack(), attackStat._base);
  }

  public void testReset() {
    Stat specDefenseStat = block.get(StatType.SPECDEFENSE);
    Stat healthStat = block.get(StatType.HEALTH);

    int specDefense = specDefenseStat.cur();
    int health = healthStat.cur();

    specDefenseStat.effect(-2);
    healthStat.effect(-10);

    assertNotSame(specDefense, specDefenseStat.cur());
    assertNotSame(health, healthStat.cur());

    block.reset();

    assertEquals(specDefense, specDefenseStat.cur());
    assertEquals(health, healthStat.cur());
  }

  public void testBurnEffectsAttack() {
    Stat attackStat = block.get(StatType.ATTACK);

    int attack = attackStat.cur();

    block.addConditionEffect(ConditionEffect.BURN);

    assertEquals(Math.max(attack / 2, 1), attackStat.cur());
  }

  public void testBurnEffectRemoval() {
    Stat attackStat = block.get(StatType.ATTACK);

    int attack = attackStat.cur();

    block.addConditionEffect(ConditionEffect.BURN);

    assertEquals(Math.max(attack / 2, 1), attackStat.cur());

    block.removeConditionEffect(ConditionEffect.BURN);

    assertEquals(attack, attackStat.cur());
  }

  public void testParalyzeEffectsSpeed() {
    Stat speedStat = block.get(StatType.SPEED);

    int speed = speedStat.cur();

    block.addConditionEffect(ConditionEffect.PARALYZE);

    assertEquals(Math.max(speed / 4, 1), speedStat.cur());
  }

  public void testRemoveMissingConditionIssue() {
    Stat attackStat = block.get(StatType.ATTACK);

    int attack = attackStat.cur();

    block.removeConditionEffect(ConditionEffect.BURN);

    assertEquals(attack, attackStat.cur());
  }

  public void testParalyzeEffectRemoval() {
    Stat speedStat = block.get(StatType.SPEED);

    int speed = speedStat.cur();

    block.addConditionEffect(ConditionEffect.PARALYZE);

    assertEquals(Math.max(speed / 4, 1), speedStat.cur());

    block.removeConditionEffect(ConditionEffect.PARALYZE);

    assertEquals(speed, speedStat.cur());
  }

  public void testAddPoint() {
    Stat defenseStat = block.get(StatType.DEFENSE);

    assertEquals(0, defenseStat.points());

    block.points(1);
    block.usePoint(StatType.DEFENSE);

    assertEquals(1, defenseStat.points());
  }

  public void testCannotAddPointWhenHaveNoPoints() {
    Stat defenseStat = block.get(StatType.DEFENSE);

    assertEquals(0, defenseStat.points());

    block.points(0);

    try {
      block.usePoint(StatType.DEFENSE);
      fail("No points to spend");
    } catch (Exception e) {
      assertTrue(e instanceof IllegalStateException);
    }

    assertEquals(0, defenseStat.points());
  }
}