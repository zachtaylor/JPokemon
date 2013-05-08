package org.jpokemon.battle;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.ActivityTracker;
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
  public BattleService getHandler() {
    return BattleService.getInstance();
  }

  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException, ServiceException {
    Activity activity = ActivityTracker.getActivity(player);

    if (!(activity instanceof BattleActivity))
      throw new ServiceException("Current activity for " + player.name() + " is not a battle");

    Battle battle = ((BattleActivity) activity).getBattle();

    response.put(getName(), battle.toJSON(player));
  }

  public Battle getBattle() {
    return _battle;
  }

  private Battle _battle;
}