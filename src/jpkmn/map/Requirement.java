package jpkmn.map;

import jpkmn.game.player.Player;

public class Requirement {
  public Requirement(RequirementType type, int value) {
    _type = type;
    _value = value;
  }

  public boolean test(Player p) {
    // TODO
    return true;
  }

  private int _value;
  private RequirementType _type;
}