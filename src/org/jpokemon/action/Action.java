package org.jpokemon.action;

import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;
import org.zachtaylor.jnodalxml.XMLNode;

public abstract class Action {
  public static final String XML_NODE_NAME = "action";

  public Action(String data) {
    _data = data;
  }

  public abstract void execute(Player player) throws LoadException;

  public String data() {
    return _data;
  }

  public XMLNode toXML() {
    return ActionFactory.toXML(this);
  }

  private String _data;
}