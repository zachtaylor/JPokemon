package jpkmn.game.item;

public enum ItemType {
  BALL, POTION, XSTAT, STONE, MACHINE, KEYITEM;

  public static ItemType valueOf(int t) {
    return ItemType.values()[t];
  }
}