package org.jpokemon.activity;

import java.util.HashMap;
import java.util.Map;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.battle.turn.TurnFactory;
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
  }

  public Battle getBattle() {
    return battle;
  }

  @Override
  public boolean onAdd(Player player) {
    int foundBattles = 0;

    for (Battle b : battles.keySet()) {
      if (b.contains(player)) {
        foundBattles++;
      }
    }

    return foundBattles == 1;
  }

  @Override
  public boolean onRemove(Player player) {
    return true;
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

      json = BattleDataGenerator.generate(player);
      json.put("view", "battle");
      json.put("turns", turns);

      PlayerManager.pushJson(player, json);
    }
  }

  private static void validate(PokemonTrainer... trainers) throws ServiceException {
    for (PokemonTrainer trainer : trainers) {
      if (trainer.party().awake() == 0) {
        throw new ServiceException("PokemonTrainer has no usable pokemon: " + trainer.toString());
      }
    }
  }
}