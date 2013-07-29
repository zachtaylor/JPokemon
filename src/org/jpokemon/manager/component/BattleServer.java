//package org.jpokemon.manager.component;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.jpokemon.activity.Activity;
//import org.jpokemon.activity.BattleActivity;
//import org.jpokemon.activity.PlayerManager;
//import org.jpokemon.battle.Battle;
//import org.jpokemon.battle.slot.Slot;
//import org.jpokemon.item.Bag;
//import org.jpokemon.item.Item;
//import org.jpokemon.manager.JPokemonServer;
//import org.jpokemon.pokemon.Pokemon;
//import org.jpokemon.pokemon.move.Move;
//import org.jpokemon.pokemon.storage.PokemonStorageUnit;
//import org.jpokemon.trainer.Player;
//import org.jpokemon.trainer.PokemonTrainer;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class BattleServer extends JPokemonServer {
//  public BattleServer(Player player) {
//    super(player);
//
//    visit(player);
//  }
//
//  /* NB: Call order: BattleServer.BattleServer > ActivityServer.ActivityServer >
//   * BattleServer.visit_player (context_owner set) > ActivityServer.visit_player
//   * etc... > visit_battle etc... */
//  @Override
//  public void visit(Player player) {
//    try {
//      trainer_json = new JSONObject();
//      trainer_json.put("id", player.id());
//
//      if (current_slot == null) {
//        data().put("player", trainer_json);
//      }
//      else {
//        team_json_by_team.get(current_slot.team()).put(trainer_json);
//      }
//    } catch (JSONException e) {
//    }
//
//    super.visit(player);
//
//    if (current_slot == null) {
//      Activity activity = PlayerManager.getActivity(player);
//      visit_battle(((BattleActivity) activity).getBattle());
//    }
//  }
//
//  @Override
//  public void visit(Bag bag) {
//    if (current_slot == null) {
//      try {
//        trainer_json.put("bag", bag_json = new JSONArray());
//      } catch (JSONException e) {
//      }
//
//      super.visit(bag);
//    }
//  }
//
//  @Override
//  public void visit(Item item) {
//    JSONObject stuff = new JSONObject();
//
//    try {
//      stuff.put("type", item.type().toString());
//      stuff.put("name", item.name());
//      stuff.put("id", item.number());
//    } catch (JSONException e) {
//    }
//
//    bag_json.put(stuff);
//  }
//
//  @Override
//  public void visit_party_leader(Pokemon pokemon) {
//    JSONObject leader_json = new JSONObject();
//
//    try {
//      leader_json.put("name", pokemon.name());
//      leader_json.put("number", pokemon.number());
//      leader_json.put("level", pokemon.level());
//      leader_json.put("hp", pokemon.health());
//      leader_json.put("hp_max", pokemon.maxHealth());
//      leader_json.put("condition", pokemon.condition().toString());
//
//      if (current_slot == null) {
//        leader_json.put("xp", pokemon.xp());
//        leader_json.put("xp_needed", pokemon.xpNeeded());
//
//        leader_json.put("moves", move_json = new JSONArray());
//      }
//
//      trainer_json.put("leader", leader_json);
//    } catch (JSONException e) {
//    }
//
//    super.visit_party_leader(pokemon);
//  }
//
//  public void visit_party(PokemonStorageUnit party) {
//    party_json = new JSONArray();
//
//    super.visit_party(party);
//
//    try {
//      trainer_json.put("pokemon", party_json);
//    } catch (JSONException e) {
//    }
//  }
//
//  @Override
//  public void visit(Pokemon pokemon) {
//    JSONObject pokemon_json = new JSONObject();
//
//    try {
//      if (pokemon.condition().isEmpty()) {
//        pokemon_json.put("ball_icon", "battle/slot_ok");
//      }
//      else {
//        pokemon_json.put("ball_icon", "battle_slot_sick");
//      }
//
//      if (current_slot == null) {
//        pokemon_json.put("number", pokemon.number());
//        pokemon_json.put("name", pokemon.name());
//
//        super.visit(pokemon);
//      }
//
//      party_json.put(pokemon_json);
//    } catch (JSONException e) {
//    }
//  }
//
//  @Override
//  public void visit(Move move) {
//    JSONObject json = new JSONObject();
//
//    try {
//      json.put("name", move.name());
//    } catch (JSONException e) {
//    }
//
//    move_json.put(json);
//  }
//
//  private void visit_battle(Battle battle) {
//    for (Slot slot : battle) {
//      visit_slot(slot);
//    }
//
//    JSONArray team_json = new JSONArray();
//    for (Map.Entry<Integer, JSONArray> team_json_entry : team_json_by_team.entrySet()) {
//      team_json.put(team_json_entry.getValue());
//    }
//
//    try {
//      data().put("teams", team_json);
//    } catch (JSONException e) {
//    }
//  }
//
//  private void visit_slot(Slot slot) {
//    if (team_json_by_team.get(slot.team()) == null) {
//      team_json_by_team.put(slot.team(), new JSONArray());
//    }
//
//    if (slot.trainer() == get_calling_player()) {
//      current_slot = null;
//
//      try {
//        // Add the player to their team
//        team_json_by_team.get(slot.team()).put(data().get("player"));
//      } catch (JSONException e) {
//      }
//    }
//    else {
//      current_slot = slot;
//      visit_pokemon_trainer(slot.trainer());
//    }
//  }
//
//  private void visit_pokemon_trainer(PokemonTrainer trainer) {
//    trainer_json = new JSONObject();
//    team_json_by_team.get(current_slot.team()).put(trainer_json);
//
//    try {
//      trainer_json.put("id", trainer.id());
//    } catch (JSONException e) {
//    }
//
//    visit_party(trainer.party());
//  }
//
//  /* current_slot == null indicates that the Player making the request is being
//   * traversed */
//  private Slot current_slot;
//  private JSONObject trainer_json;
//  private JSONArray bag_json, party_json, move_json;
//  private Map<Integer, JSONArray> team_json_by_team = new HashMap<Integer, JSONArray>();
//}