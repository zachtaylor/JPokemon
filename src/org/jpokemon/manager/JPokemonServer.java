package org.jpokemon.manager;

import org.jpokemon.AbstractJPokemonVisitor;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public abstract class JPokemonServer extends AbstractJPokemonVisitor {
  public JPokemonServer(Player player) {
    setData(new JSONObject());

    calling_player = player;
  }

  protected Player get_calling_player() {
    return calling_player;
  }

  public JSONObject data() {
    return (JSONObject) data;
  }

  public void setData(Object object) {
    if (object instanceof JSONObject) {
      data = object;
    }
    else {
      throw new IllegalArgumentException("Data must be of type JSONObject");
    }
  }

  private Object data;
  private Player calling_player;
}