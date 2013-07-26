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
  public static final int TRAINER_PARTY_SIZE = 6;
  public static final int PLAYER_STORAGE_UNIT_COUNT = 8;
  public static final int PLAYER_STORAGE_UNIT_SIZE = 20;

  // Pokemon config
  public static final int KNOWN_MOVE_COUNT = 4;
  public static final int STAT_POINTS_PER_LEVEL = 1;
  public static final int STAT_POINTS_PER_EVOLUTION = 0;
  public static final int STAT_POINTS_INDIVIDUAL_MAX = 100;
  public static final double STAT_MAX_VALUE_WEIGHT_IV = 1.0;
  public static final double STAT_MAX_VALUE_WEIGHT_EV = 0.25;
  public static final double STAT_MAX_VALUE_WEIGHT_BASE = 2.0;
  public static final double STAT_MAX_VALUE_WEIGHT_POINTS = 0.5;
  public static final boolean MEASURE_EFFORT_VALUE_REALTIME = false;

  // Battle config
  public static final int SHIELD_ROUND_DURATION = 5;
  public static final double SHIELD_REDUCTION_MODIFIER = 1.0;
  public static final boolean ALLOW_REPEAT_TRAINER_BATTLES = false;
}