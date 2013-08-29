package org.jpokemon.activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.jpokemon.action.CreateBattleAction;
import org.jpokemon.action.FriendsAction;
import org.jpokemon.provider.BattleDataProvider;
import org.jpokemon.provider.FriendsDataProvider;
import org.jpokemon.provider.OverworldDataProvider;
import org.jpokemon.server.JPokemonServer;
import org.jpokemon.server.JPokemonWebSocket;
import org.jpokemon.server.Message;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;
import org.zachtaylor.jnodalxml.XmlParser;

public class PlayerManager {
  public static Player getPlayer(String name) {
    return players.get(name);
  }

  public static Activity getActivity(Player player) {
    Stack<Activity> stack = activities.get(player);
    return stack.peek();
  }

  public static void addActivity(Player player, Activity a) {
    if (a.onAdd(player)) {
      activities.get(player).add(a);
    }
  }

  public static void popActivity(Player player, Activity a) {
    if (activities.get(player).peek() == a && a.onRemove(player)) {
      activities.get(player).pop();
    }
    else {
      throw new IllegalStateException("Popped activity is not most recent");
    }
  }

  public static void pushMessage(Player player, Message message) {
    pushJson(player, message.toJson());
  }

  public static void pushJson(Player player, JSONObject json) {
    JPokemonWebSocket webSocket;

    synchronized (players) {
      webSocket = reverseConnections.get(player);
    }

    webSocket.sendJson(json);
  }

  public static void dispatchRequest(JPokemonWebSocket socket, JSONObject request) throws JSONException,
      ServiceException {
    Player player;

    synchronized (players) {
      player = connections.get(socket);
    }

    if (player == null) {
      if (request.has("login")) {
        login(socket, request);
      }
      else {
        throw new ServiceException("Missing credentials");
      }
    }
    else if (request.has("load")) {
      String dataRef = request.getString("load");

      if ("friends".equals(dataRef)) {
        pushJson(player, FriendsDataProvider.generate(player));
      }
      else if ("battle".equals(dataRef)) {
        pushJson(player, BattleDataProvider.generate(player));
      }
      else if ("overworld".equals(dataRef)) {
        pushJson(player, OverworldDataProvider.generate(player));
      }
    }
    else {
      Activity activity = getActivity(player);

      if (request.has("action") && activity.supportsAction(request.getString("action"))) {
        if ("friends".equals(request.getString("action"))) {
          new FriendsAction(request).execute(player);
        }
        else if ("createbattle".equals(request.getString("action"))) {
          new CreateBattleAction(request).execute(player);
        }
        else {
          throw new ServiceException("Unidentified action: " + request.getString("action"));
        }
      }
      else {
        activity.handleRequest(player, request);
      }
    }
  }

  public static void close(JPokemonWebSocket socket) {
    synchronized (players) {
      Player player = connections.get(socket);
      if (player == null) { return; }

      File file = new File(JPokemonServer.savepath, player.id() + ".jpkmn");

      try {
        Writer writer = new BufferedWriter(new PrintWriter(file));
        writer.write(player.toXml().printToString(0, "\t"));
        writer.close();
      }
      catch (IOException e) {
        e.printStackTrace();
      }

      connections.remove(socket);
      reverseConnections.remove(player);
      players.remove(player.id());

      while (!activities.get(player).isEmpty()) {
        PlayerManager.popActivity(player, PlayerManager.getActivity(player));
      }
      activities.remove(player); // doesn't need synchronous but oh well :)
    }
  }

  private static void login(JPokemonWebSocket socket, JSONObject request) throws JSONException, ServiceException {
    String name = request.getString("login");

    if (players.keySet().contains(name)) { throw new ServiceException("File already loaded"); }
    File file = new File(JPokemonServer.savepath, name + ".jpkmn");
    if (!file.exists()) { throw new ServiceException("Save file not found"); }

    Player player = new Player(name);

    try {
      player.loadXML(XmlParser.parse(file).get(0));
    }
    catch (IndexOutOfBoundsException e) {
      e.printStackTrace();
    }
    catch (FileNotFoundException e) {
    }

    synchronized (players) {
      players.put(name, player);
      connections.put(socket, player);
      reverseConnections.put(player, socket);
    }

    activities.put(player, new Stack<Activity>());
    addActivity(player, OverworldActivity.getInstance());
  }

  /** Lock on players for players, connections, reverseConnections */
  private static volatile Map<String, Player> players = new HashMap<String, Player>();
  private static volatile Map<JPokemonWebSocket, Player> connections = new HashMap<JPokemonWebSocket, Player>();
  private static volatile Map<Player, JPokemonWebSocket> reverseConnections = new HashMap<Player, JPokemonWebSocket>();

  private static Map<Player, Stack<Activity>> activities = new HashMap<Player, Stack<Activity>>();
}