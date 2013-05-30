package org.jpokemon.manager.component;

import org.jpokemon.manager.Activity;
import org.jpokemon.trainer.Player;

public class OverworldActivity implements Activity {
  private OverworldActivity() {
  }

  public static OverworldActivity getInstance() {
    return instance;
  }

  public String getName() {
    return "overworld";
  }

  @Override
  public OverworldService getHandler() {
    return OverworldService.getInstance();
  }

  public OverworldServer getServer(Player player) {
    return new OverworldServer(player);
  }

  public static OverworldActivity instance = new OverworldActivity();
}