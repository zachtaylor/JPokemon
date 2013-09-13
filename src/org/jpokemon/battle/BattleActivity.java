package org.jpokemon.battle;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.battle.turn.TurnFactory;
import org.jpokemon.provider.BattleDataProvider;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleActivity implements Activity {
  Battle battle;

  public BattleActivity(PokemonTrainer... trainers) throws ServiceException {
    validate(trainers);

    battle = new Battle(trainers);
  }

  public Battle getBattle() {
    return battle;
  }

  @Override
  public boolean onAdd(Player player) {
    return true;
  }

  @Override
  public boolean onRemove(Player player) {
    return true;
  }

  public Battle getBattle(PokemonTrainer trainer) {
    return battle;
  }

  @Override
  public boolean supportsAction(String action) {
    return false;
  }

  @Override
  public void handleRequest(Player player, JSONObject request) throws JSONException, ServiceException {
    String trainerID = request.getString("id");
    String targetID = request.getString("target");

    Slot slot = battle.getSlot(trainerID);
    Slot target = battle.getSlot(targetID);

    Turn turn = TurnFactory.create(request, slot, target);
    battle.addTurn(turn);

    pushState();
  }

  public void pushState() throws JSONException, ServiceException {
    Turn turn;
    Player player;
    JSONObject json;
    JSONObject turns = new JSONObject();

    for (Slot slot : battle) {
      turn = battle.getTurn(slot);

      if (turn == null) {
        turns.put(slot.trainer().id(), "missing");
      }
      else {
        turns.put(slot.trainer().id(), "submitted");
      }
    }

    for (Slot slot : battle) {
      if (!(slot.trainer() instanceof Player)) {
        continue;
      }
      player = (Player) slot.trainer();

      json = BattleDataProvider.generate(player);
      json.put("view", "battle");
      json.put("turns", turns);

      PlayerManager.pushJson(player, json);
    }
  }

  private static void validate(PokemonTrainer... trainers) throws ServiceException {
    for (PokemonTrainer trainer : trainers) {
      if (trainer.party().awake() == 0) { throw new ServiceException("PokemonTrainer has no usable pokemon: " + trainer.toString()); }
    }
  }
}