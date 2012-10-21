package org.jpokemon.pokemon.stat;

import jpkmn.Constants;

public class Stat {
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
    doModify();
  }

  public void effect(int power) {
    _delta += power;

    if (Math.abs(_delta) > Constants.STATCHANGEMAX)
      _delta = (int) Math.copySign(Constants.STATCHANGEMAX, _delta);

    if (_delta > 0)
      _cur = (int) ((_max * ((2.0 + _delta)/2)));
    else if (_delta < 0)
      _cur = (int) (_max * 2.0 / (2 + -_delta));
    else
      _cur = _max;

    doModify();
  }

  public void reset() {
    _cur = _max = (int) (((2.0 * _base + _pts) * _level) / 100.0 + 5.0);
    modify(1);
  }

  private void doModify() {
    _cur = (int) Math.max(_cur * _modifier, 1);
  }

  protected double _modifier;
  protected int _delta, _cur, _max, _base, _pts, _level;
}