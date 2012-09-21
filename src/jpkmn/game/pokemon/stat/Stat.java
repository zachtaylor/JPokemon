package jpkmn.game.pokemon.stat;

import jpkmn.Constants;

public class Stat {
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
    _delta += power;

    if (_delta > Constants.STATCHANGEMAX)
      _delta = Constants.STATCHANGEMAX;
    else if (_delta < Constants.STATCHANGEMAX)
      _delta = -Constants.STATCHANGEMAX;

    if (_delta > 0)
      _cur = _max * ((_delta + 2) / 2);
    else if (_delta < 0) {
      _cur = (int) (Math.pow((3.0 / 4.0), -_delta) * _max);
      if (_cur < 0) _cur = 1;
    }
    else
      _cur = _max;
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

  protected int _cur, _max, _base, _pts, _lvl, _delta;
}