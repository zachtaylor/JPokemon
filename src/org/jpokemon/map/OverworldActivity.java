package org.jpokemon.map;

import org.jpokemon.activity.Activity;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.trainer.Player;

public class OverworldActivity extends JPokemonService implements Activity {
  private OverworldActivity() {
  }

  public static OverworldActivity getInstance() {
    return instance;
  }

  public String getName() {
    return "overworld";
  }

  @Override
  public MapService getHandler() {
    return MapService.getInstance();
  }

  public MapServer getServer(Player player) {
    return new MapServer(player);
  }

  public static OverworldActivity instance = new OverworldActivity();
}