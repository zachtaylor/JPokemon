package org.jpokemon.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.StatType;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.jpokemon.trainer.TrainerState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerService extends JPokemonService {
  public static JSONObject pull(JSONObject request) throws ServiceException {
    Player p = getPlayer(request);

    JSONObject response = new JSONObject();

    try {
      response.put("state", p.state().toString());
      response.put("messages", loadMessagesForPlayer(p));
      p.state().appendDataToResponse(response, request, p);

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return response;
  }

  public static int load(String name) throws ServiceException {
    Player p = null;

    try {
      p = PlayerFactory.load(name);

      _messageQueues.put(p, new LinkedList<String>());

      p.state(TrainerState.OVERWORLD);
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }

    return p.id();
  }

  public static int create(String name, String rivalName) {
    Player p = PlayerFactory.create(name, rivalName);
    _messageQueues.put(p, new LinkedList<String>());
    p.state(TrainerState.OVERWORLD);

    return p.id();
  }

  public static void save(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);

    PlayerFactory.save(player);
  }

  public static void party(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);

    if (player.state() == TrainerState.OVERWORLD) {
      player.state(TrainerState.UPGRADE);
      return;
    }
    if (player.state() != TrainerState.UPGRADE) {
      return; // TODO : throw error
    }
    if (!request.has("stats")) {
      player.state(TrainerState.OVERWORLD);
      return;
    }

    Pokemon pokemon = getPokemon(request);

    try {
      JSONArray stats = request.getJSONArray("stats");
      for (int i = 0; i < stats.length(); i++) {
        StatType s = StatType.valueOf(stats.getJSONObject(i).getString("stat"));
        pokemon.statPoints(s, stats.getJSONObject(i).getInt("amount"));
      }
    } catch (JSONException e) {
    }
  }

  public static void addToMessageQueue(Player p, String message) {
    Queue<String> q = _messageQueues.get(p);
    q.add(message);
  }

  public static JSONArray loadMessagesForPlayer(Player p) {
    JSONArray array = new JSONArray();
    while (!_messageQueues.get(p).isEmpty())
      array.put(_messageQueues.get(p).remove());
    return array;
  }

  private static Map<Player, Queue<String>> _messageQueues = new HashMap<Player, Queue<String>>();
}