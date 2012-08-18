package jpkmn.game.item;

public enum ItemType {
  BALL, POTION, MACHINE, STONE, XSTAT, KEYITEM;

  public static ItemType valueOf(int t) {
    return ItemType.values()[t];
  }
}