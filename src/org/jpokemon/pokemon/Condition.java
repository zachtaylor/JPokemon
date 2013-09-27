package org.jpokemon.pokemon;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zachtaylor.jnodalxml.XmlNode;

public class Condition {
  public static final String XML_NODE_NAME = "condition";

  public Condition() {
    _lastMessage = new ArrayList<String>();
    _effects = new ArrayList<ConditionEffect>();
  }

  /**
   * Adds a new issue to the Pokemon. If already afflicted, the effect is refreshed.
   * 
   * @param e The issue to be added
   */
  public void add(ConditionEffect e) {
    if (contains(e)) remove(e);

    _effects.add(e);
  }

  public boolean contains(ConditionEffect e) {
    return _effects.contains(e);
  }

  public boolean remove(ConditionEffect e) {
    return _effects.remove(e);
  }

  /**
   * Cleans up all status ailments.
   */
  public void reset() {
    _effects = new ArrayList<ConditionEffect>();
  }

  /**
   * Computes the catch bonus for effects on the Pokemon. 20 if FRZ or SLP, 15 if BRN, PSN, or PAR. 10 normally.
   * 
   * @return the Catch Bonus
   */
  public double getCatchBonus() {
    double best = 1;

    for (ConditionEffect e : _effects)
      best *= e.catchBonus();

    return Math.min(best, 2.0);
  }

  /**
   * Checks against the user's effects to see if they can attack. Returns true if they can.
   * 
   * @return true if user can attack
   */
  public boolean canAttack() {
    for (ConditionEffect i : _effects) {
      if (i.blocksAttack()) return false;
    }

    return true;
  }

  /**
   * Applies issues. DOTs hurt, Flinch is removed, volatile effects have a chance to dispel.
   */
  public void applyEffects(Pokemon p) {
    resetMessage();

    for (Iterator<ConditionEffect> conditionEffectIterator = _effects.iterator(); conditionEffectIterator.hasNext();) {
      ConditionEffect current = conditionEffectIterator.next();

      boolean persist = Math.random() <= current.persistanceChance();
      String message = current.persistanceMessage(persist);

      if (persist) {
        p.takeDamage((int) (p.maxHealth() * current.damagePercentage()));
      }
      else {
        conditionEffectIterator.remove();
      }

      if (message != null) pushMessage(p.name() + message);
    }
  }

  public String[] lastMessage() {
    return _lastMessage.toArray(new String[_lastMessage.size()]);
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    node.setValue(toString());

    return node;
  }

  public String toString() {
    if (_effects.isEmpty()) return "";
    if (_effects.size() == 1) return _effects.get(0).toString();
    return _effects.toString();
  }

  private void resetMessage() {
    _lastMessage = new ArrayList<String>();
  }

  private void pushMessage(String s) {
    _lastMessage.add(s);
  }

  private List<String> _lastMessage;
  private List<ConditionEffect> _effects;
}