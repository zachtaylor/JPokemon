package org.jpokemon.pokemon.stat;

public class Health extends Stat {
  @Override
  public void reset() {
    _cur = _max = 10 + _level + ((2 * _base + _pts) * _level) / 100;
  }

  public int max() {
    return _max;
  }

  @Override
  public void effect(int power) {
    _cur += power;

    if (_cur > _max)
      _cur = _max;
    else if (_cur < 0)
      _cur = 0;
  }
}