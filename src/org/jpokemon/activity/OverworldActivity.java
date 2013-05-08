package org.jpokemon.activity;

import org.jpokemon.map.Area;
import org.jpokemon.map.Border;
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

public class OverworldActivity extends JPokemonService implements Activity {
  private OverworldActivity() {
  }

  public String getName() {
    return "overworld";
  }

  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    String option = getOption(request);

    if (option.equals("npc"))
      npc(request);
    else if (option.equals("border"))
      border(request);
    else if (option.equals("grass"))
      grass(request);
  }

  public void appendDataToResponse(JSONObject response, JSONObject request, Player player) throws JSONException,
      ServiceException {
    Area area = getArea(request);

    response.put(getName(), area.toJSON(player));
  }

  public static OverworldActivity getInstance() {
    if (instance == null)
      instance = new OverworldActivity();

    return instance;
  }

  private void npc(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    NPC npc = getNpc(request);
    String option = getNPCOption(request);

    try {
      npc.actionset(option).execute(player);
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }
  }

  private void border(JSONObject request) throws ServiceException {
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

  private void grass(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Area area = getArea(request);

    Pokemon pokemon = area.pokemon();

    if (pokemon == null)
      throw new ServiceException("No wild pokemon in this area");

    PokemonTrainer trainer = new WildTrainer();
    trainer.add(pokemon);

    ActivityTracker.setActivity(player, new BattleActivity(player, trainer));
  }

  public static OverworldActivity instance;
}