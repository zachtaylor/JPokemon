package org.jpokemon.interaction;

import org.jpokemon.trainer.Player;

public interface Requirement {
  public boolean isSatisfied(Player player);
}