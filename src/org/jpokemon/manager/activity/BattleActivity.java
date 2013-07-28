package org.jpokemon.manager.activity;

import org.jpokemon.battle.Battle;
import org.jpokemon.manager.Activity;
import org.jpokemon.manager.LoadException;
import org.jpokemon.manager.ServiceException;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleActivity implements Activity {
  public BattleActivity(PokemonTrainer... trainers) throws LoadException {
    validate(trainers);

    battle = Battle.create(trainers);
    battle.start();
  }

  public Battle getBattle() {
    return battle;
  }

  @Override
  public void handleRequest(Player player, JSONObject request) throws JSONException, ServiceException {
    battle.createTurn(request);
  }

  private static void validate(PokemonTrainer... trainers) throws LoadException {
    for (PokemonTrainer trainer : trainers) {
      if (trainer.party().awake() == 0) {
        throw new LoadException("PokemonTrainer has no usable pokemon: " + trainer.toString());
      }
    }
  }

  private Battle battle;
}