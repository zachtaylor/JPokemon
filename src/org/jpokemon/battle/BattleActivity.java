package org.jpokemon.battle;

import org.jpokemon.activity.Activity;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;

public class BattleActivity implements Activity {
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

  @Override
  public BattleServer getServer(Player player) {
    return new BattleServer(player);
  }

  public Battle getBattle() {
    return _battle;
  }

  private Battle _battle;
}