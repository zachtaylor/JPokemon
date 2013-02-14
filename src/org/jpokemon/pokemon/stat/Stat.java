package org.jpokemon.pokemon.stat;

import org.jpokemon.JPokemonConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class Stat implements JPokemonConstants {
  public Stat() {
    _level = 1;
    _modifier = 1;
    _iv = (int) (Math.random() * INDIVIDUAL_VALUE_RANGE_CAP);
  }

  public int cur() {
    return _cur;
  }

  public int ev() {
    return _ev + _evPending;
  }

  public void ev(int val) {
    if (!MEASURE_EFFORT_VALUE_REALTIME) {
      _evPending += val;
    }
    else {
      _ev += val;
      computeMax();
      computeCur();
    }
  }

  public int iv() {
    return _iv;
  }

  public void iv(int val) {
    _iv = val;

    computeMax();
    computeCur();
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

    if (!MEASURE_EFFORT_VALUE_REALTIME) {
      _ev += _evPending;
      _evPending = 0;
    }

    reset();
  }

  public void base(int b) {
    _base = b;
    reset();
  }

  public void modify(double m) {
    _modifier = m;

    computeCur();
  }

  public void effect(int power) {
    _delta += power;

    if (Math.abs(_delta) > STAT_CHANGE_MAX_DELTA)
      _delta = (int) Math.copySign(STAT_CHANGE_MAX_DELTA, _delta);

    computeCur();
  }

  public void reset() {
    _delta = 0;

    computeMax();
    computeCur();
  }

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();

    try {
      data.put("cur", _cur);
      data.put("max", _max);
      data.put("points", _pts);

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  private void computeMax() {
    double val = (STAT_MAX_VALUE_WEIGHT_BASE * _base);
    val += (STAT_MAX_VALUE_WEIGHT_IV * _iv);
    val += (STAT_MAX_VALUE_WEIGHT_EV * _ev);
    val += (STAT_MAX_VALUE_WEIGHT_POINTS * _pts);
    val += 100;
    val /= 100;
    val *= _level;
    val += 10;

    _max = (int) val;
  }

  private void computeCur() {
    if (_delta > 0)
      _cur = (int) ((_max * ((2.0 + _delta) / 2)));
    else if (_delta < 0)
      _cur = (int) (_max * 2.0 / (2 + -_delta));
    else
      _cur = _max;

    _cur = (int) Math.max(_cur * _modifier, 1);
  }

  protected double _modifier;
  protected int _delta, _cur, _max, _base, _pts, _level, _ev, _evPending, _iv;
}