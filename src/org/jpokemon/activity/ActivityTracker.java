package org.jpokemon.activity;

import java.util.HashMap;
import java.util.Map;

import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;

public class ActivityTracker {
  public static Activity getActivity(PokemonTrainer trainer) {
    if (trainer instanceof Player)
      return data.get(trainer);

    return null;
  }

  public static void clearActivity(PokemonTrainer trainer) {
    if (trainer instanceof Player)
      setActivity(trainer, OverworldActivity.getInstance());
  }

  public static void setActivity(PokemonTrainer trainer, Activity a) {
    // TODO : validation

    data.put(trainer, a);
  }

  public static void stopTracking(PokemonTrainer trainer) {
    data.remove(trainer);
  }

  private static Map<PokemonTrainer, Activity> data = new HashMap<PokemonTrainer, Activity>();
}