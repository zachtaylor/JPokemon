package org.jpokemon.activity;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SelectMoveFromPokemonActivity implements Activity {
  private Pokemon pokemon;
  private int moveIndex = -1;

  public SelectMoveFromPokemonActivity(Pokemon p) {
    pokemon = p;
  }

  public int getMoveIndex() {
    return moveIndex;
  }

  @Override
  public void onAdd(Player player) throws ServiceException {
    JSONObject json = new JSONObject();
    List<JSONObject> moves = new ArrayList<JSONObject>();

    try {
      json.put("action", "selectmove");
      json.put("pokemon", pokemon.name());

      for (int i = 0; i < pokemon.moveCount(); i++) {
        JSONObject moveJson = new JSONObject();
        Move move = pokemon.move(i);

        moveJson.put("name", move.name());
        moveJson.put("pp", move.pp());
        moveJson.put("ppMax", move.ppMax());

        moves.add(moveJson);
      }

      json.put("moves", new JSONArray(moves.toString()));
    }
    catch (JSONException e) {
    }

    PlayerManager.pushJson(player, json);
  }

  @Override
  public void logout(Player player) {
  }

  @Override
  public void onReturn(Activity activity, Player player) {
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    try {
      if (request.has("moveIndex")) {
        moveIndex = request.getInt("moveIndex");

        PlayerManager.popActivity(player, this);
      }
      else {
        throw new ServiceException("Expected move index");
      }
    }
    catch (JSONException e) {
      throw new ServiceException("Expected move index");
    }
  }
}