package jpkmn.map;

import org.jpokemon.player.Player;

/**
 * A generalized rule, which can be used to test a Player
 * 
 * @author zach
 */
public class Requirement {
  private enum Type {
    EVENT, BADGE;

    public static Requirement.Type valueOf(int r) {
      return Requirement.Type.values()[r];
    }
  }

  public Requirement(int type, int value) {
    _type = Type.valueOf(type);
    _value = value;
  }

  public boolean test(Player p) {
    switch (_type) {
    case EVENT:
      return p.events().get(_value);
    case BADGE:
      return p.badge() >= _value;
    }
    return true;
  }

  private int _value;
  private Requirement.Type _type;
}