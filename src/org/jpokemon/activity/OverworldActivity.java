package org.jpokemon.activity;

import org.jpokemon.overworld.map.Area;
import org.jpokemon.overworld.map.Border;
import org.jpokemon.overworld.map.Map;
import org.jpokemon.overworld.npc.NPC;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.WildTrainer;
import org.json.JSONException;
import org.json.JSONObject;

public class OverworldActivity implements Activity {
  public static OverworldActivity instance = new OverworldActivity();

  private OverworldActivity() {
  }

  public static OverworldActivity getInstance() {
    return instance;
  }

  @Override
  public boolean onAdd(Player player) {
    PlayerManager.pushJson(player, "overworld");
    return true;
  }

  @Override
  public boolean onRemove(Player player) { // Useful hook for issue#69
    return false;
  }

  @Override
  public boolean supportsAction(String action) {
    return true;
  }

  @Override
  public void handleRequest(Player player, JSONObject request) throws JSONException, ServiceException {
    // TODO
  }

  private void handleNPCRequest(Player player, JSONObject request) throws ServiceException {
    NPC npc = getNpc(player, request);
    String option = getNPCOption(request);

    npc.actionset(option).execute(player);
  }

  private void handleBorderRequest(Player player, JSONObject request) throws ServiceException {
    Border border = getBorder(player, request);

    if (border.performAction(player)) {
      player.setArea(border.getNext());
    }
    else {
      throw new ServiceException("You cannot go that way");
    }
  }

  private void handleGrassRequest(Player player, JSONObject request) throws ServiceException {
    Area area = getArea(player, request);

    Pokemon pokemon = area.pokemon();

    if (pokemon == null) { throw new ServiceException("No wild pokemon in this area"); }

    PokemonTrainer trainer = new WildTrainer();
    trainer.add(pokemon);

    PlayerManager.addActivity(player, new BattleActivity(player, trainer));
  }

  private NPC getNpc(Player player, JSONObject request) throws ServiceException {
    int npcNumber = -1;
    NPC npc = null;
    Area area = null;

    area = getArea(player, request);

    try {
      npcNumber = request.getInt("npc");
    } catch (JSONException e) {
      throw new ServiceException("Npc number not found");
    }

    npc = area.getNpc(npcNumber);

    if (npc == null)
      throw new ServiceException("Npc number " + npcNumber + " not found");

    return npc;
  }

  private String getNPCOption(JSONObject request) throws ServiceException {
    String npc_option = null;

    try {
      npc_option = request.getString("option");
    } catch (JSONException e) {
      throw new ServiceException("NPC option not found");
    }

    return npc_option;
  }

  private Area getArea(Player player, JSONObject request) throws ServiceException {
    Area area = Map.area(player.getArea());

    if (area == null) { throw new ServiceException("Area number " + player.getArea() + " not found"); }

    return area;
  }

  private Border getBorder(Player player, JSONObject request) throws ServiceException {
    Area area;
    String borderChoice;
    Border border;

    area = getArea(player, request);

    try {
      borderChoice = request.getString("border");
    } catch (JSONException e) {
      throw new ServiceException("Border choice not found");
    }

    border = area.getBorder(borderChoice);

    if (border == null) { throw new ServiceException("Border not found in area"); }

    return border;
  }
}