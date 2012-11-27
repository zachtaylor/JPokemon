package jpkmn.game.item;

import jpkmn.game.battle.Target;

public enum ItemType {
  BALL, POTION, XSTAT, STONE, MACHINE, KEYITEM;

  public static ItemType valueOf(int t) {
    return ItemType.values()[t];
  }

  public Target target() {
    switch (this) {
    case POTION: case XSTAT: case STONE: case MACHINE: case KEYITEM:
      return Target.SELF;
    case BALL:
      return Target.ENEMY;
    default:
      return null;
    }
  }
}