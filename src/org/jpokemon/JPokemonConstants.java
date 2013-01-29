package org.jpokemon;

/**
 * System settings
 */
public interface JPokemonConstants {

  // Environment config
  public static final String SAVE_PATH = "data/save/";
  public static final String DATABASE_PATH = "data/Pokemon.db";

  // Player config
  public static final int STARTER_POKEMON_LEVEL = 5;
  public static final int KNOWN_MOVE_COUNT = 4;
  public static final int TRAINER_PARTY_SIZE = 6;
  public static final int PLAYER_STORAGE_UNIT_COUNT = 8;
  public static final int PLAYER_STORAGE_UNIT_SIZE = 20;

  // Battle config
  public static final int STAT_CHANGE_MAX_DELTA = 6;
  public static final int SHIELD_ROUND_DURATION = 5;
  public static final double TYPE_ADVANTAGE_MODIFIER = 2.0;
  public static final double SHIELD_REDUCTION_MODIFIER = 1.0;
  public static final double TYPE_DISADVANTAGE_MODIFIER = 0.5;
  public static final double GYM_EXPERIENCE_MODIFIER = 1.7;
  public static final double TRAINER_EXPERIENCE_MODIFIER = 1.3;
  public static final double UNIVERSAL_EXPERIENCE_MODIFIER = 1.0;
  public static final boolean ALLOW_REPEAT_TRAINER_BATTLES = false;

  // Useless stuff I am going to get rid of
  public static final int ITEMNUMBER = 19;
  public static final int POKEMONNUMBER = 151;
  public static final int MOVENUMBER = 164;

}