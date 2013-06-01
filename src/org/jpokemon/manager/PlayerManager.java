package org.jpokemon.manager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.manager.component.OverworldActivity;
import org.jpokemon.manager.message.Message;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.zachtaylor.jnodalxml.XMLParser;

public class PlayerManager {
  public static JSONObject getDataRequest(JSONObject request) throws ServiceException {
    Player player = JPokemonService.getPlayer(request);
    Activity activity = getActivity(player);

    JSONObject response = new JSONObject();

    try {
      response.put("state", activity.getName());
      response.put("messages", loadMessagesForPlayer(player));
      response.put(activity.getName(), activity.getServer(player).data());

    } catch (JSONException e) {
      e.printStackTrace();
    }

    return response;
  }

  public static void activityRequest(JSONObject request) throws ServiceException {
    Player player = JPokemonService.getPlayer(request);
    Activity activity = getActivity(player);

    JPokemonService service = activity.getHandler();

    if (request.has("option")) {
      try {
        service.handleRequestOption(request.getString(OPTION_KEY), request);
      } catch (JSONException e) {
      }
    }
    else {
      service.handleRequest(request);
    }
  }

  public static Player getPlayer(String id) {
    if (players.get(id) == null)
      throw new IllegalArgumentException("Could not retrieve PlayerID: " + id);

    return players.get(id);
  }

  public static String loadPlayer(String name) throws ServiceException {
    if (players.keySet().contains(name))
      throw new ServiceException("File already loaded");

    File file = new File(JPokemonConstants.SAVE_PATH + name + ".jpkmn");

    if (!file.exists())
      throw new ServiceException("Save file not found");

    Player player = newPlayer(name);

    try {
      player.loadXML(XMLParser.parse(file).get(0));
    } catch (FileNotFoundException e) {
    }

    messageQueues.put(player, new LinkedList<Message>());
    setActivity(player, OverworldActivity.getInstance());
    return player.id();
  }

  public static String createPlayer(String name, String rivalName) {
    Player player = newPlayer(name = getUniquePlayerName(name));
    player.name(name);
    player.record().setRivalName(rivalName);
    messageQueues.put(player, new LinkedList<Message>());
    setActivity(player, OverworldActivity.getInstance());
    return player.id();
  }

  public static void savePlayer(Player player) {
    String path = JPokemonConstants.SAVE_PATH + player.id() + ".jpkmn";

    try {
      Writer writer = new BufferedWriter(new PrintWriter(new File(path)));
      writer.write(player.toXML().toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static Activity getActivity(PokemonTrainer trainer) {
    if (trainer instanceof Player) {
      return activities.get((Player) trainer);
    }

    return null;
  }

  public static void setActivity(PokemonTrainer trainer, Activity a) {
    // TODO : validation

    if (trainer instanceof Player) {
      activities.put((Player) trainer, a);
    }
  }

  public static void clearActivity(PokemonTrainer trainer) {
    if (trainer instanceof Player) {
      setActivity(trainer, OverworldActivity.getInstance());
    }
  }

  public static void addMessageToQueue(Player p, Message message) {
    Queue<Message> q = messageQueues.get(p);
    q.add(message);
  }

  private static JSONArray loadMessagesForPlayer(Player p) {
    JSONArray array = new JSONArray();

    while (!messageQueues.get(p).isEmpty()) {
      array.put(messageQueues.get(p).remove().toJSON());
    }

    return array;
  }

  private static Player newPlayer(String id) {
    Player player = new Player(id);
    players.put(id, player);

    return player;
  }

  private static String getUniquePlayerName(String attempt) {
    if (!players.containsKey(attempt) && !new File(JPokemonConstants.SAVE_PATH + attempt + ".jpkmn").exists())
      return attempt;

    int n = 0;
    for (; players.containsKey(attempt + n) || new File(JPokemonConstants.SAVE_PATH + attempt + n + ".jpkmn").exists(); n++)
      ;

    return attempt + n;
  }

  private static Map<String, Player> players = new HashMap<String, Player>();
  private static Map<Player, Activity> activities = new HashMap<Player, Activity>();
  private static Map<Player, Queue<Message>> messageQueues = new HashMap<Player, Queue<Message>>();

  private static final String OPTION_KEY = "option";
}