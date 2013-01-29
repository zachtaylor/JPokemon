package org.jpokemon.trainer;

import org.jpokemon.JPokemonConstants;

public enum TrainerType implements JPokemonConstants {
  PLAYER, GYM, RIVAL, TRAINER, WILD;

  public static TrainerType valueOf(int t) {
    return TrainerType.values()[t];
  }
}