package jpkmn.game.player;

import org.jpokemon.JPokemonConstants;

public enum TrainerType implements JPokemonConstants {
  PLAYER, GYM, RIVAL, TRAINER, WILD;

  public static TrainerType valueOf(int t) {
    return TrainerType.values()[t];
  }

  public double xpFactor() {
    switch (this) {
    case GYM:
      return GYM_EXPERIENCE_MODIFIER;
    case RIVAL: case TRAINER:
      return TRAINER_EXPERIENCE_MODIFIER;
    case PLAYER:
      return 0;
    default:
      return 1.0;
    }
  }
}