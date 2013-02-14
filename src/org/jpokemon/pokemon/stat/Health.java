package org.jpokemon.pokemon.stat;

public class Health extends Stat {
  @Override
  public void effect(int power) {
    _cur += power;

    if (_cur > _max)
      _cur = _max;
    else if (_cur < 0)
      _cur = 0;
  }
}