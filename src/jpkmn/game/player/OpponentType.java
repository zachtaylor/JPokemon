package jpkmn.game.player;

public enum OpponentType {
  GYM, RIVAL, TRAINER, WILD;

  public static OpponentType valueOf(int t) {
    return OpponentType.values()[t];
  }
}