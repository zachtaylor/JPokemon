package org.jpokemon.activity;

import org.jpokemon.battle.Battle;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleActivity extends JPokemonService implements Activity {
  public BattleActivity(PokemonTrainer... trainers) {
    _battle = Battle.create(trainers);
    _battle.start();
  }

  public String getName() {
    return "battle";
  }

  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    Battle battle = getBattle(request);

    battle.createTurn(request);
  }

  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    Battle battle = getBattle(request);

    response.put(getName(), battle.toJSON(player));
  }

  public Battle getBattle() {
    return _battle;
  }

  private Battle _battle;
}