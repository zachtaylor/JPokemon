package org.jpokemon.battle.activity;

import org.jpokemon.activity.Activity;
import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.turn.SwapTurn;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.pokemon.ConditionEffect;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BuildSwapTurnActivity implements BuildTurnActivity {
  private Battle battle;
  private String slotId;
  private int pokemonIndex = -1;

  public BuildSwapTurnActivity(Battle b) {
    battle = b;
  }

  @Override
  public void onAdd(Player player) throws ServiceException {
    slotId = player.id();

    JSONObject json = new JSONObject();

    try {
      json.put("action", "selectpokemon");

      JSONArray pokemonArray = new JSONArray();
      json.put("pokemon", pokemonArray);

      for (Pokemon pokemon : player.party()) {
        JSONObject pokemonJson = new JSONObject();

        pokemonJson.put("name", pokemon.name());
        pokemonJson.put("pokemonNumber", pokemon.number());
        pokemonJson.put("level", pokemon.level());
        pokemonJson.put("health", pokemon.health());
        pokemonJson.put("healthMax", pokemon.maxHealth());

        JSONArray conditionArray = new JSONArray();
        for (ConditionEffect conditionEffect : pokemon.getConditionEffects()) {
          conditionArray.put(conditionEffect.name());
        }
        pokemonJson.put("condition", conditionArray);

        pokemonArray.put(pokemonJson);
      }
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
      if (request.has("pokemonIndex")) {
        pokemonIndex = request.getInt("pokemonIndex");

        JSONObject closeJson = new JSONObject();
        closeJson.put("action", "selectpokemon:close");
        PlayerManager.pushJson(player, closeJson);

        PlayerManager.popActivity(player, this);
        return;
      }
    }
    catch (JSONException e) {
    }

    throw new ServiceException("Expected pokemon index");
  }

  @Override
  public Turn getTurn() {
    if (pokemonIndex == -1) { return null; }

    Slot userSlot = battle.getSlot(slotId);

    return new SwapTurn(battle, userSlot, pokemonIndex);
  }
}