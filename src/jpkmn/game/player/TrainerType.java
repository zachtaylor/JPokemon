package jpkmn.game.player;

public enum TrainerType {
  GYM, RIVAL, TRAINER;

  public static TrainerType valueOf(int t) {
    return TrainerType.values()[t];
  }
}