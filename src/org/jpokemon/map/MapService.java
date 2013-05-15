package org.jpokemon.map;

import org.jpokemon.activity.ActivityService;
import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.battle.BattleActivity;
import org.jpokemon.map.npc.NPC;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.LoadException;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.WildTrainer;
import org.json.JSONException;
import org.json.JSONObject;

public class MapService extends JPokemonService implements ActivityService {
  private static final String NPC_NUMBER_KEY = "number", NPC_OPTION_KEY = "npc_option";

  private MapService() {
  }

  public static MapService getInstance() {
    return instance;
  }

  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    throw new ServiceException("MapService has no default handler");
  }

  @Override
  public void handleRequestOption(String option, JSONObject request) throws ServiceException {
    if (option.equals("npc"))
      handleNPCRequest(request);
    else if (option.equals("border"))
      handleBorderRequest(request);
    else if (option.equals("grass"))
      handleGrassRequest(request);
  }

  private void handleNPCRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    NPC npc = getNpc(request);
    String option = getNPCOption(request);

    try {
      npc.actionset(option).execute(player);
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }
  }

  private void handleBorderRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Border border = getBorder(request);

    String reason = border.isOkay(player);
    if (reason == null) {
      player.area(border.getNext());
    }
    else {
      throw new ServiceException(reason);
    }
  }

  private void handleGrassRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Area area = getArea(request);

    Pokemon pokemon = area.pokemon();

    if (pokemon == null)
      throw new ServiceException("No wild pokemon in this area");

    PokemonTrainer trainer = new WildTrainer();
    trainer.add(pokemon);

    ActivityTracker.setActivity(player, new BattleActivity(player, trainer));
  }

  private Area getArea(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Area area = Map.area(player.area());

    if (area == null)
      throw new ServiceException("Area number " + player.area() + " not found");

    return area;
  }

  private NPC getNpc(JSONObject request) throws ServiceException {
    int npcNumber = -1;
    NPC npc = null;
    Area area = null;

    area = getArea(request);

    try {
      npcNumber = request.getInt(NPC_NUMBER_KEY);
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
      npc_option = request.getString(NPC_OPTION_KEY);
    } catch (JSONException e) {
      throw new ServiceException("NPC option not found");
    }

    return npc_option;
  }

  private Border getBorder(JSONObject request) throws ServiceException {
    Area area;
    String borderChoice;
    Border border;

    area = getArea(request);

    try {
      borderChoice = request.getString("border");
    } catch (JSONException e) {
      throw new ServiceException("Border choice not found");
    }

    border = area.getBorder(borderChoice);

    if (border == null)
      throw new ServiceException("Border not found in area");

    return border;
  }

  private static MapService instance = new MapService();
}