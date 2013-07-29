package org.jpokemon.activity;

import java.util.HashMap;
import java.util.Map;

import org.jpokemon.battle.Battle;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleActivity implements Activity {
  private Battle battle;
  private static final Map<Battle, BattleActivity> battles = new HashMap<Battle, BattleActivity>();

  public BattleActivity(PokemonTrainer... trainers) throws ServiceException {
    validate(trainers);

    battles.put(battle, this);

    battle = new Battle(trainers);
    battle.start();
  }

  public Battle getBattle() {
    return battle;
  }

  @Override
  public void onAdd(Player player) { // Battle is too complicated for this
  }

  @Override
  public void onRemove(Player player) { // Battle is too complicated for this
  }

  public static void removePlayer(Battle battle, Player player) {
    BattleActivity activity = battles.get(battle);
    PlayerManager.popActivity(player, activity);

    if (battle.getPlayerCount() < 1) {
      battles.remove(battle);
    }
  }

  @Override
  public void handleRequest(Player player, JSONObject request) throws JSONException, ServiceException {
    battle.createTurn(request);
  }

  private static void validate(PokemonTrainer... trainers) throws ServiceException {
    for (PokemonTrainer trainer : trainers) {
      if (trainer.party().awake() == 0) {
        throw new ServiceException("PokemonTrainer has no usable pokemon: " + trainer.toString());
      }
    }
  }
}