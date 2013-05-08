package org.jpokemon.activity;

import org.jpokemon.battle.Battle;
import org.jpokemon.service.BattleService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleActivity implements Activity {
  public BattleActivity(PokemonTrainer... trainers) {
    _battle = Battle.create(trainers);
    _battle.start();
  }

  public String getName() {
    return "battle";
  }

  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    response.put(getName(), BattleService.info(request));
  }

  public Battle getBattle() {
    return _battle;
  }

  private Battle _battle;
}