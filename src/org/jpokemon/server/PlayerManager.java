package org.jpokemon.server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.jpokemon.activity.Activity;
import org.jpokemon.battle.lobby.LobbyService;
import org.jpokemon.extra.FriendsService;
import org.jpokemon.overworld.OverworldService;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;
import org.zachtaylor.jnodalxml.XmlParser;

public class PlayerManager {
  public static boolean playerIsLoggedIn(String name) {
    boolean result;

    synchronized (players) {
      result = players.containsKey(name);
    }

    return result;
  }

  public static boolean playerExists(String name) {
    if (playerIsLoggedIn(name)) { return true; }

    String filename = name + ".jpkmn";

    File file = new File(JPokemonServer.savepath, filename);
    if (!file.exists() || !file.getName().equals(filename)) { return false; }

    return true;
  }

  public static Player getPlayer(String name) {
    Player player;

    synchronized (players) {
      player = players.get(name);
    }

    return player;
  }

  public static void bootstrapServices() {
    services = new HashMap<String, JPokemonService>();
    services.put("overworld", new OverworldService());
    services.put("lobby", new LobbyService());
    services.put("friends", new FriendsService());
  }

  public static boolean hasActivity(Player player) {
    return !activities.get(player.id()).isEmpty();
  }

  public static Activity getActivity(Player player) {
    Stack<Activity> stack = activities.get(player.id());
    return stack.peek();
  }

  public static void addActivity(Player player, Activity a) {
    try {
      activities.get(player.id()).add(a);
      a.onAdd(player);
    }
    catch (ServiceException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void popActivity(Player player, Activity a) {
    if (getActivity(player) != a) { throw new IllegalStateException("Popped activity is not most recent"); }

    activities.get(player.id()).pop();

    if (hasActivity(player)) {
      getActivity(player).onReturn(a, player);
    }
  }

  public static void pushMessage(Player player, Message message) {
    pushJson(player, message.toJson());
  }

  public static void pushJson(Player player, JSONObject json) {
    if (json == null) { return; }

    JPokemonWebSocket webSocket;

    synchronized (players) {
      webSocket = sockets.get(player.id());
    }

    webSocket.sendJson(json);
  }

  public static void dispatch(JPokemonWebSocket socket, JSONObject request) throws JSONException, ServiceException {
    String playerId;
    synchronized (players) {
      playerId = sessions.get(socket);
    }

    if (playerId == null) {
      login(socket, request);
      return;
    }

    Player player = getPlayer(playerId);

    if (request.has("load")) {
      String serviceName = request.getString("load");
      JPokemonService service = services.get(serviceName);

      PlayerManager.pushJson(player, service.load(request, player));
    }
    else if (hasActivity(player)) {
      Activity a = getActivity(player);

      a.serve(request, player);
    }
    else if (request.has("service")) {
      String serviceName = request.getString("service");
      JPokemonService service = services.get(serviceName);

      service.serve(request, player);
    }
  }

  public static void close(JPokemonWebSocket socket) {
    String playerId;
    synchronized (players) {
      playerId = sessions.get(socket);
    }

    if (playerId == null) { return; }

    Player player = getPlayer(playerId);
    File file = new File(JPokemonServer.savepath, playerId + ".jpkmn");

    try {
      Writer writer = new BufferedWriter(new PrintWriter(file));
      writer.write(player.toXml().printToString(0, "\t"));
      writer.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }

    for (JPokemonService service : services.values()) {
      service.logout(player);
    }

    while (hasActivity(player)) {
      activities.get(player.id()).pop().logout(player);
    }
    activities.remove(playerId);

    synchronized (players) {
      sessions.remove(socket);
      sockets.remove(playerId);
      players.remove(playerId);
    }
  }

  private static void login(JPokemonWebSocket socket, JSONObject request) throws JSONException, ServiceException {
    if (!request.has("login")) { throw new ServiceException("No credentials found"); }

    String name = request.getString("login");

    if (playerIsLoggedIn(name)) { throw new ServiceException("File already loaded"); }
    if (!playerExists(name)) { throw new ServiceException("Save file not found"); }

    Player player = new Player(name);
    String filename = name + ".jpkmn";
    File file = new File(JPokemonServer.savepath, filename);

    try {
      player.loadXML(XmlParser.parse(file).get(0));
    }
    catch (FileNotFoundException e) {
    }

    synchronized (players) {
      players.put(name, player);
      sockets.put(name, socket);
      sessions.put(socket, name);
    }

    for (JPokemonService service : services.values()) {
      service.login(player);
    }

    activities.put(name, new Stack<Activity>());
  }

  private static Map<String, JPokemonService> services;

  /** Lock on players for players, connections, reverseConnections */
  private static volatile Map<String, Player> players = new HashMap<String, Player>();
  private static volatile Map<String, JPokemonWebSocket> sockets = new HashMap<String, JPokemonWebSocket>();
  private static volatile Map<JPokemonWebSocket, String> sessions = new HashMap<JPokemonWebSocket, String>();

  private static Map<String, Stack<Activity>> activities = new HashMap<String, Stack<Activity>>();
}