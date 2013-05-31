package org.jpokemon.manager.component;

import org.jpokemon.manager.JPokemonService;
import org.jpokemon.manager.LoadException;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.ServiceException;
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

public class OverworldService extends JPokemonService {
  private static final String NPC_NUMBER_KEY = "number", NPC_OPTION_KEY = "npc_option";

  private OverworldService() {
  }

  public static OverworldService getInstance() {
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
    else if (option.equals("save"))
      handleSaveRequest(request);
  }

  private void handleNPCRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    NPC npc = getNpc(request);
    String option = getNPCOption(request);

    try {
      npc.actionset(option).execute(player);
    } catch (LoadException e) {
      throw new ServiceException(e);
    }
  }

  private void handleBorderRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Border border = getBorder(request);

    try {
      if (border.performAction(player)) {
        player.area(border.getNext());
      }
      else {
        throw new ServiceException("You cannot go that way");
      }
    } catch (LoadException e) {
      throw new ServiceException(e);
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

    try {
      PlayerManager.setActivity(player, new BattleActivity(player, trainer));
    } catch (LoadException e) {
      throw new ServiceException(e);
    }
  }

  private void handleSaveRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);

    PlayerManager.savePlayer(player);
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

  private static OverworldService instance = new OverworldService();
}