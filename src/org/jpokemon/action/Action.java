package org.jpokemon.action;

import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;

public abstract class Action {
  public Action() {

  }

  public Action(String s) {
    data = s;
  }

  public abstract void execute(Player player) throws ServiceException;

  protected String getData() {
    return data;
  }

  private String data;
}