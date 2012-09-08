package jpkmn.map;

import jpkmn.game.player.Player;

public class Requirement {
  private enum RequirementType {
    MOVE, BADGE, POKEDEX, ITEM;

    static RequirementType valueOf(int r) {
      return RequirementType.values()[r];
    }
  }

  public Requirement(int type, int value) {
    _type = RequirementType.valueOf(type);
    _value = value;
  }

  public boolean test(Player p) {
    // TODO
    return true;
  }

  private int _value;
  private RequirementType _type;
}