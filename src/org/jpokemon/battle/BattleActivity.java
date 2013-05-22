package org.jpokemon.battle;

import org.jpokemon.activity.Activity;
import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;

public class BattleActivity implements Activity {
  public BattleActivity(PokemonTrainer... trainers) throws LoadException {
    validate(trainers);

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

  @Override
  public BattleServer getServer(Player player) {
    return new BattleServer(player);
  }

  public Battle getBattle() {
    return _battle;
  }

  private static void validate(PokemonTrainer... trainers) throws LoadException {
    for (PokemonTrainer trainer : trainers) {
      if (trainer.party().awake() == 0) {
        throw new LoadException("PokemonTrainer has no usable pokemon: " + trainer.toString());
      }
    }
  }

  private Battle _battle;
}