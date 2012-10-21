package org.jpokemon.pokemon.stat;

import jpkmn.game.base.PokemonBase;
import jpkmn.game.pokemon.Condition;

public class StatBlock {
  public StatBlock(PokemonBase base) {
    _data = new Stat[StatType.values().length];

    _data[0] = new Health();
    for (int i = 1; i < _data.length; i++)
      _data[i] = new Stat();

    rebase(base);
  }

  /**
   * Gets the StatType specified
   * 
   * @param type StatType to fetch
   * @return Stat specified by the type
   */
  public Stat get(StatType type) {
    return _data[type.ordinal()];
  }

  /**
   * Resets each stat
   */
  public void reset() {
    for (Stat s : _data)
      s.reset();
  }

  /**
   * Applies a new level to each Stat
   * 
   * @param level New level of the Pokemon which has these stats
   */
  public void level(int level) {
    for (Stat s : _data)
      s.level(level);
  }

  /**
   * Get the total number of unused Stat Points
   * 
   * @return Free stat point count
   */
  public int points() {
    return _points;
  }

  /**
   * Set the total number of unused Stat Points
   * 
   * @param p Amount of points
   */
  public void points(int p) {
    _points = p;
  }

  /**
   * Sets up each stat to use the value from the PokemonBase specified
   * 
   * @param base PokemonBase which has the new base values for each stat
   */
  public void rebase(PokemonBase base) {
    // This is not polymorphic, because that would make the database ugly

    _data[0].base(base.getHealth());
    _data[StatType.ATTACK.ordinal()].base(base.getAttack());
    _data[StatType.SPECATTACK.ordinal()].base(base.getSpecattack());
    _data[StatType.DEFENSE.ordinal()].base(base.getDefense());
    _data[StatType.SPECDEFENSE.ordinal()].base(base.getSpecdefense());
    _data[StatType.SPEED.ordinal()].base(base.getSpeed());
  }

  /**
   * Applies a stat penalty, as a result of a condition issue
   * 
   * @param i The issue which applies a stat penalty
   */
  public void addIssue(Condition.Issue i) {
    // Also not polymorhpic, because condition issues don't map a stat

    if (i == Condition.Issue.BURN) {
      _burn = true;
      _data[StatType.ATTACK.ordinal()].modify(1.0 / 2.0);
    }
    else if (i == Condition.Issue.PARALYZE) {
      _paralyze = true;
      _data[StatType.SPEED.ordinal()].modify(1.0 / 4.0);
    }
  }

  /**
   * Removes the previously added stat penalty of a condition issue
   * 
   * @param i The issue to reset the effects of
   */
  public void removeIssue(Condition.Issue i) {
    if (i == Condition.Issue.BURN && _burn) {
      _burn = false;
      _data[StatType.ATTACK.ordinal()].modify(1);
    }
    else if (i == Condition.Issue.PARALYZE && _paralyze) {
      _paralyze = false;
      _data[StatType.SPEED.ordinal()].modify(1);
    }
  }

  private int _points;
  private Stat[] _data;
  private boolean _burn, _paralyze;
}