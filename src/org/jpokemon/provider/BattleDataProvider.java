package org.jpokemon.provider;

import java.util.HashMap;
import java.util.Map;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.PlayerManager;
import org.jpokemon.battle.Battle;
import org.jpokemon.battle.BattleActivity;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.item.Bag;
import org.jpokemon.item.Item;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleDataProvider extends JPokemonVisitor {
  private Slot current_slot;
  private Player player_context;
  private JSONObject trainer_json, data = new JSONObject();
  private JSONArray bag_json, party_json, move_json;
  private Map<Integer, JSONArray> team_json_by_team = new HashMap<Integer, JSONArray>();

  private BattleDataProvider() {
  }

  public static JSONObject generate(Player player) {
    BattleDataProvider bdg = new BattleDataProvider();

    try {
      bdg.visit(bdg.player_context = player);
    } catch (JSONException e) {
    }

    return bdg.data;
  }

  /* NB: Call order: BattleServer.BattleServer > ActivityServer.ActivityServer >
   * BattleServer.visit_player (context_owner set) > ActivityServer.visit_player
   * etc... > visit_battle etc... */
  @Override
  public void visit(Player player) throws JSONException {
    trainer_json = new JSONObject();
    trainer_json.put("id", player.id());

    if (current_slot == null) {
      data.put("player", trainer_json);
    }
    else {
      team_json_by_team.get(current_slot.team()).put(trainer_json);
    }

    try {
      super.visit(player);
    } catch (Exception e) {
    }

    if (current_slot == null) {
      Activity activity = PlayerManager.getActivity(player);
      visit_battle(((BattleActivity) activity).getBattle());
    }
  }

  @Override
  public void visit(Bag bag) throws JSONException {
    if (current_slot == null) {
      trainer_json.put("bag", bag_json = new JSONArray());

      try {
        super.visit(bag);
      } catch (Exception e) {
      }
    }
  }

  @Override
  public void visit(Item item) throws JSONException {
    JSONObject stuff = new JSONObject();

    stuff.put("type", item.type().toString());
    stuff.put("name", item.name());
    stuff.put("id", item.number());

    bag_json.put(stuff);
  }

  @Override
  public void visit_party_leader(Pokemon pokemon) throws JSONException {
    JSONObject leader_json = new JSONObject();

    leader_json.put("name", pokemon.name());
    leader_json.put("number", pokemon.number());
    leader_json.put("level", pokemon.level());
    leader_json.put("hp", pokemon.health());
    leader_json.put("hp_max", pokemon.maxHealth());
    leader_json.put("condition", pokemon.condition().toString());

    if (current_slot == null) {
      leader_json.put("xp", pokemon.xp());
      leader_json.put("xp_needed", pokemon.xpNeeded());

      leader_json.put("moves", move_json = new JSONArray());
    }

    trainer_json.put("leader", leader_json);

    try {
      super.visit_party_leader(pokemon);
    } catch (Exception e) {
    }
  }

  @Override
  public void visit_party(PokemonStorageUnit party) throws JSONException {
    party_json = new JSONArray();

    try {
      super.visit_party(party);
    } catch (Exception e) {
    }

    trainer_json.put("pokemon", party_json);
  }

  @Override
  public void visit(Pokemon pokemon) throws JSONException {
    JSONObject pokemon_json = new JSONObject();

    if (pokemon.condition().isEmpty()) {
      pokemon_json.put("ball_icon", "battle/slot_ok");
    }
    else {
      pokemon_json.put("ball_icon", "battle_slot_sick");
    }

    if (current_slot == null) {
      pokemon_json.put("number", pokemon.number());
      pokemon_json.put("name", pokemon.name());

      try {
        super.visit(pokemon);
      } catch (Exception e) {
      }
    }

    party_json.put(pokemon_json);
  }

  @Override
  public void visit(Move move) throws JSONException {
    JSONObject json = new JSONObject();

    json.put("name", move.name());

    move_json.put(json);
  }

  private void visit_battle(Battle battle) throws JSONException {
    for (Slot slot : battle) {
      visit_slot(slot);
    }

    JSONArray team_json = new JSONArray();
    for (Map.Entry<Integer, JSONArray> team_json_entry : team_json_by_team.entrySet()) {
      team_json.put(team_json_entry.getValue());
    }

    data.put("teams", team_json);
  }

  private void visit_slot(Slot slot) throws JSONException {
    if (team_json_by_team.get(slot.team()) == null) {
      team_json_by_team.put(slot.team(), new JSONArray());
    }

    if (slot.trainer() == player_context) {
      current_slot = null;

      // Add the player to their team
      team_json_by_team.get(slot.team()).put(data.get("player"));
    }
    else {
      current_slot = slot;
      visit_pokemon_trainer(slot.trainer());
    }
  }

  private void visit_pokemon_trainer(PokemonTrainer trainer) throws JSONException {
    trainer_json = new JSONObject();
    team_json_by_team.get(current_slot.team()).put(trainer_json);

    trainer_json.put("id", trainer.id());

    visit_party(trainer.party());
  }
}