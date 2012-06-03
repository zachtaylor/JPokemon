package jpkmn.pokemon.stat;

import jpkmn.Constants;

public abstract class Stat {
  public Stat(int base, int lvl) {
    _base = base;
    _lvl = lvl;
  }

  public int cur() {
    return _cur;
  }

  public int max() {
    return _max;
  }

  public int pts() {
    return _pts;
  }

  public void setPts(int p) {
    _pts = p;
  }
  
  public double percentage() {
    return ((double) _cur) / ((double) _max);
  }

  /**
   * Increases the temporary version by max/2
   */
  public void increase() {
    if (_delta == Constants.STATCHANGEMAX) return;

    _delta++;
    _cur += _max / 2;
  }

  /**
   * Decreases the temporary version by *= 3/4
   */
  public void decrease() {
    if (_delta == -Constants.STATCHANGEMAX) return;

    _delta--;
    _cur *= 3 / 4;
    if (_cur < 1) _cur = 1;
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
   * Sets the temporary version the max version
   */
  public void reset() {
    _cur = _max;
  }

  public abstract void resetMax();

  /**
   * Increases the points spent, calls resetMax();
   */
  public void usePoint() {
    _pts++;
    resetMax();
  }

  /**
   * Sets the stored lvl variable, and resets the max and current values.
   * 
   * @param l New value for the stored lvl variable
   */
  public void setLevel(int l) {
    _lvl = l;
    resetMax();
    reset();
  }

  public void setBase(int b) {
    _base = b;
    resetMax();
    reset();
  }

  int _cur, _max, _base, _pts, _lvl, _delta;
}
