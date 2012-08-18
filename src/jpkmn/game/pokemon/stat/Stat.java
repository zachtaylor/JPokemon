package jpkmn.game.pokemon.stat;

import jpkmn.Constants;

public class Stat {
  public Stat() {
    resetMax();
    _cur = _max;
  }

  public int cur() {
    return _cur;
  }

  public int max() {
    return _max;
  }

  public int points() {
    return _pts;
  }

  public void points(int p) {
    _pts = p;
    resetMax();
  }

  /**
   * Changes the current value of a stat with respect to the max, either up or
   * down, based on the power of the change. Negative for decreases.
   * 
   * @param power
   */
  public void effect(int power) {
    for (int i = 0; i < power; ++i)
      increase();
    for (int i = 0; i > power; --i)
      decrease();

    if (_cur < 0) _cur = 1;
  }

  /**
   * Sets the current value to the max value
   */
  public void reset() {
    _cur = _max;
  }

  /**
   * Recalculates the maximum value based on the base value, number of stat
   * points invested, and level
   */
  public void resetMax() {
    _max = ((2 * _base + _pts) * _lvl) / 100 + 5;
  }

  /**
   * Sets the stored lvl variable, and resets the max and current values.
   * 
   * @param l New value for the stored lvl variable
   */
  public void level(int l) {
    _lvl = l;
    resetMax();
    reset();
  }

  public void rebase(int b) {
    _base = b;
    resetMax();
    reset();
  }

  /**
   * Increases the current value of this stat by max/2, according to formula
   */
  private void increase() {
    if (_delta == Constants.STATCHANGEMAX) return;

    _delta++;
    _cur += _max / 2;
  }

  /**
   * Decreases the current value of this stat by cur/4, according to forumla
   */
  private void decrease() {
    if (_delta == -Constants.STATCHANGEMAX) return;

    _delta--;
    _cur *= 3 / 4;
    if (_cur < 1) _cur = 1;
  }

  protected int _cur, _max, _base, _pts, _lvl, _delta;
}