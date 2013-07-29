//package org.jpokemon.manager.component;
//
//import org.jpokemon.manager.JPokemonServer;
//import org.jpokemon.pokemon.Pokemon;
//import org.jpokemon.pokemon.stat.Stat;
//import org.jpokemon.pokemon.storage.PokemonStorageUnit;
//import org.jpokemon.trainer.Player;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class UpgradeServer extends JPokemonServer {
//  public UpgradeServer(Player player) {
//    super(player);
//
//    visit(player);
//  }
//
//  @Override
//  public void visit_party(PokemonStorageUnit unit) {
//    try {
//      data().put("party", party = new JSONArray());
//    } catch (JSONException e) {
//      return;
//    }
//
//    super.visit_party(unit);
//  }
//
//  @Override
//  public void visit(PokemonStorageUnit unit) { // Nothing
//  }
//
//  @Override
//  public void visit(Pokemon pokemon) {
//    JSONObject json = new JSONObject();
//
//    try {
//      json.put("name", pokemon.name());
//      json.put("number", pokemon.number());
//      json.put("points", pokemon.availableStatPoints());
//      json.put("stats", stats = new JSONArray());
//    } catch (JSONException e) {
//      return;
//    }
//
//    party.put(json);
//
//    super.visit(pokemon);
//  }
//
//  @Override
//  public void visit(Stat stat) {
//    JSONObject json = new JSONObject();
//
//    try {
//      json.put("name", last_stat_type().toString());
//      json.put("max", stat.max());
//      json.put("ev", stat.ev());
//      json.put("points", stat.points());
//      json.put("iv", stat.iv());
//    } catch (JSONException e) {
//      return;
//    }
//
//    stats.put(json);
//
//    super.visit(stat);
//  }
//
//  private JSONArray party, stats;
//}