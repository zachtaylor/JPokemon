package org.jpokemon.action.performer;

import org.jpokemon.manager.LoadException;
import org.jpokemon.trainer.Player;

public interface ActionPerformer {
  public void execute(Player player) throws LoadException;
}