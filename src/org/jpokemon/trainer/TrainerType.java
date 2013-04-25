package org.jpokemon.trainer;

public enum TrainerType {
  PLAYER, GYM, RIVAL, TRAINER, WILD;

  public static TrainerType valueOf(int t) {
    return TrainerType.values()[t];
  }
}