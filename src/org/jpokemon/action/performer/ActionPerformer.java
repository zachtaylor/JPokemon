package org.jpokemon.action.performer;

import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;

public interface ActionPerformer {
  public void execute(Player player) throws LoadException;
}