package jpkmn.map;

import jpkmn.game.player.Player;

/**
 * A generalized rule, which can be used to test a Player
 * 
 * @author zach
 */
public class Requirement {
  public enum Type {
    MOVE, BADGE, POKEDEX, ITEM;

    public static Requirement.Type valueOf(int r) {
      return Requirement.Type.values()[r];
    }
  }

  public Requirement(int type, int value) {
    _type = Type.valueOf(type);
    _value = value;
  }

  public boolean test(Player p) {
    // TODO
    return true;
  }

  private int _value;
  private Requirement.Type _type;
}