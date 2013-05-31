package org.jpokemon.manager;

import org.jpokemon.trainer.Player;

public interface Activity {
  public String getName();

  public JPokemonService getHandler();

  public JPokemonServer getServer(Player player);
}