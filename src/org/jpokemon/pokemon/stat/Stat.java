package org.jpokemon.pokemon.stat;

import org.jpokemon.JPokemonConstants;

public class Stat implements JPokemonConstants {
  public Stat() {
    _level = 1;
    _modifier = 1;
  }

  public int cur() {
    return _cur;
  }

  public int points() {
    return _pts;
  }

  public void points(int p) {
    _pts = p;
    reset();
  }

  public void level(int l) {
    _level = l;
    reset();
  }

  public void base(int b) {
    _base = b;
    reset();
  }

  public void modify(double m) {
    _modifier = m;

    doCalculation();
  }

  public void effect(int power) {
    _delta += power;

    if (Math.abs(_delta) > STATCHANGEMAX)
      _delta = (int) Math.copySign(STATCHANGEMAX, _delta);

    doCalculation();
  }

  public void reset() {
    _max = (int) (((2.0 * _base + _pts) * _level) / 100.0 + 5.0);
    _delta = 0;

    doCalculation();
  }

  private void doCalculation() {
    if (_delta > 0)
      _cur = (int) ((_max * ((2.0 + _delta) / 2)));
    else if (_delta < 0)
      _cur = (int) (_max * 2.0 / (2 + -_delta));
    else
      _cur = _max;

    _cur = (int) Math.max(_cur * _modifier, 1);
  }

  protected double _modifier;
  protected int _delta, _cur, _max, _base, _pts, _level;
}