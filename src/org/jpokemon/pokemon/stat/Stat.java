package org.jpokemon.pokemon.stat;

import org.zachtaylor.jnodalxml.XmlException;
import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.myna.Myna;

public class Stat {
  public static final String XML_NODE_NAME = "stat";

  public static int ivmax = 32;

  public static int delta = 6;

  public static double baseweight = 2.0;

  public static double evweight = 0.25;

  public static double ivweight = 1.0;

  public static double bonusweight = 1.0;

  static {
    Myna.configure(Stat.class, "org.jpokemon.pokemon.stat");
  }

  public Stat() {
    _level = 1;
    _modifier = 1;
    _iv = (int) (Math.random() * ivmax);
  }

  public int cur() {
    return _cur;
  }

  public int max() {
    return _max;
  }

  public int ev() {
    return _ev;
  }

  public void ev(int val) {
    _ev += val;
    computeMax();
    computeCur();
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

    if (Math.abs(_delta) > delta) {
      _delta = (int) Math.copySign(delta, _delta);
    }

    computeCur();
  }

  public void reset() {
    _delta = 0;

    computeMax();
    computeCur();
  }

  public XmlNode toXml() {
    XmlNode myNode = new XmlNode(XML_NODE_NAME);

    myNode.setAttribute("cur", _cur);
    myNode.setAttribute("max", _max);
    myNode.setAttribute("points", _pts);
    myNode.setAttribute("ev", _ev);
    myNode.setAttribute("iv", _iv);
    myNode.setSelfClosing(true);

    return myNode;
  }

  public void loadXml(XmlNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XmlException("Cannot read node");

    _cur = node.getIntAttribute("cur");
    _max = node.getIntAttribute("max");
    _pts = node.getIntAttribute("points");
    _ev = node.getIntAttribute("ev");
    _iv = node.getIntAttribute("iv");
  }

  private void computeMax() {
    double val = (baseweight * _base);
    val += (ivweight * _iv);
    val += (evweight * _ev);
    val += (bonusweight * _pts);
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
  protected int _delta, _cur, _max, _base, _pts, _level, _ev, _iv;
}