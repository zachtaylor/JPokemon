package org.jpokemon.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.ActivityService;
import org.jpokemon.activity.ActivityTracker;
import org.jpokemon.map.OverworldActivity;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerService extends JPokemonService {
  public static JSONObject pull(JSONObject request) throws ServiceException {
    Player p = getPlayer(request);
    Activity a = getActivity(request);

    JSONObject response = new JSONObject();

    try {
      response.put("state", a.getName());
      response.put("messages", loadMessagesForPlayer(p));
      response.put(a.getName(), a.getServer(p).data());

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return response;
  }

  public static String load(String name) throws ServiceException {
    Player p = null;

    try {
      p = PlayerFactory.load(name);
      _messageQueues.put(p, new LinkedList<String>());
      ActivityTracker.setActivity(p, OverworldActivity.getInstance());
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }

    return p.id();
  }

  public static String create(String name, String rivalName) {
    Player p = PlayerFactory.create(name, rivalName);
    _messageQueues.put(p, new LinkedList<String>());
    ActivityTracker.setActivity(p, OverworldActivity.getInstance());

    return p.id();
  }

  public static void save(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);

    PlayerFactory.save(player);
  }

  public static void activity(JSONObject request) throws ServiceException {
    Activity activity = getActivity(request);

    ActivityService service = activity.getHandler();

    if (request.has("option")) {
      service.handleRequestOption(getOption(request), request);
    }
    else {
      service.handleRequest(request);
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