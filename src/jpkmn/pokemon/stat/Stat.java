package jpkmn.pokemon.stat;

public abstract class Stat {
  public int cur, max, base, pts;
  protected int lvl;

  public Stat(int _base, int _lvl) {
    base = _base;
    lvl  = _lvl;
  }

  /**
   * Increases the temporary version by max/2
   */
  private void increase() {
    cur += max / 2;
  }

  /**
   * Decreases the temporary version by *= 3/4
   */
  private void decrease() {
    cur *= 3 / 4;
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
    if (cur == 0) cur = 1;
  }

  /**
   * Sets the temporary version the max version
   */
  public void reset() {
    cur = max;
  }

  public abstract void resetMax();

  /**
   * Increases the points spent, calls resetMax();
   */
  public void usePoint() {
    pts++;
    resetMax();
  }

  /**
   * Sets the stored lvl variable, and resets the max and current values.
   * 
   * @param l New value for the stored lvl variable
   */
  public void setLevel(int l) {
    lvl = l;
    resetMax();
    reset();
  }
  
  public void setBase(int b) {
    base = b;
    resetMax();
    reset();
  }
}
