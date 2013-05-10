package org.jpokemon.activity;

import org.jpokemon.AbstractJPokemonVisitor;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public abstract class ActivityServer extends AbstractJPokemonVisitor {
  public ActivityServer(Player player) {
    setData(new JSONObject());

    visit_player(player);
  }

  @Override
  public JSONObject data() {
    return (JSONObject) super.data();
  }

  public void setData(Object object) {
    if (object instanceof JSONObject) {
      super.setData(object);
    }
    else {
      throw new IllegalArgumentException("Data must be of type JSONObject");
    }
  }
}