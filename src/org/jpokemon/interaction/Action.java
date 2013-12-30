package org.jpokemon.interaction;

import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public interface Action {
  public void execute(Player player) throws ServiceException;
}