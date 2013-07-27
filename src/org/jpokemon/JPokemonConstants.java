package org.jpokemon;

/**
 * System settings
 */
public class JPokemonConstants {
  // Environment config
  public static final String SAVE_PATH = "data/save/";
  public static final String IMAGE_PATH = "data/image/";
  public static final String TRAINER_PATH = "data/trainer/";
  public static final String DATABASE_PATH = "data/Pokemon.db";

  // Player config
  public static final int STARTER_POKEMON_LEVEL = 5;

  // Pokemon config
  public static final boolean MEASURE_EFFORT_VALUE_REALTIME = false;

  // Battle config
  public static final int SHIELD_ROUND_DURATION = 5;
  public static final double SHIELD_REDUCTION_MODIFIER = 1.0;
  public static final boolean ALLOW_REPEAT_TRAINER_BATTLES = false;
}