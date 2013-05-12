package org.jpokemon.activity;

import org.jpokemon.trainer.Player;

public interface Activity {
  public String getName();

  public ActivityService getHandler();

  public ActivityServer getServer(Player player);
}