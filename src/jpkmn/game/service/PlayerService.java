package jpkmn.game.service;

import jpkmn.exceptions.LoadException;
import jpkmn.exceptions.ServiceException;
import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.AreaRegistry;
import jpkmn.map.Direction;
import jpkmn.map.Event;

import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerService {
  public static JSONObject loadPlayer(String name) throws ServiceException {
    Player p;

    try {
      p = PlayerFactory.load(name);
    } catch (LoadException e) {
      e.printStackTrace();
      throw new ServiceException(e.getMessage());
    }

    try {
      JSONObject data = JSONMaker.make(p);
      p.setState("world");
      return data;
    } catch (JSONException jsone) {
      jsone.printStackTrace();
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static JSONObject savePlayer(int playerID) throws ServiceException {
    try {
      PlayerFactory.save(playerID);
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }

    try {
      return JSONMaker.make(PlayerFactory.get(playerID));
    } catch (JSONException e) {
      e.printStackTrace();
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static JSONObject newPlayer(String name, String starter)
      throws ServiceException {
    int pokemon = 0;
    if (starter.equals("Bulbasaur"))
      pokemon = 1;
    else if (starter.equals("Charmander"))
      pokemon = 4;
    else if (starter.equals("Squirtle"))
      pokemon = 7;
    else
      throw new ServiceException("Starter not supported: " + starter);

    try {
      Player player = PlayerFactory.create(name, pokemon);
      return JSONMaker.make(player);
    } catch (JSONException e) {
      e.printStackTrace();
      throw new ServiceException("There was an error. It's not your fault.");
    } catch (Exception e) {
      e.printStackTrace();
      throw new ServiceException(e.getMessage());
    }
  }

  public static JSONArray starterPokemon() {
    JSONArray json = new JSONArray();

    json.put("Bulbasaur");
    json.put("Charmander");
    json.put("Squirtle");

    return json;
  }

  public static JSONObject pokemonInfo(int pID, int i) throws ServiceException {
    Player player = PlayerFactory.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    try {
      return JSONMaker.make(player.party().get(i));
    } catch (IllegalArgumentException e) {
      throw new ServiceException(player.name() + " has no " + i + " Pokemon");
    } catch (JSONException e) {
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static JSONObject areaInfo(int playerID) throws ServiceException {
    Player player = PlayerFactory.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no registered area!");

    try {
      return JSONMaker.make(area);
    } catch (JSONException e) {
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static void areaChange(int pID, String dir) throws ServiceException {
    Player player = PlayerFactory.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Direction direction = Direction.valueOf(dir);

    if (direction == null)
      throw new ServiceException("Direction " + dir + "not recognized");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no registered area!");

    AreaConnection connection = area.neighbor(direction);

    if (connection == null)
      throw new ServiceException(area.name() + " cannot access " + direction);
    if (!connection.test(player))
      throw new ServiceException("You are not qualified to go  " + direction);

    connection.use(player);
  }

  public static void triggerEvent(int pID, int eID) throws ServiceException {
    Player player = PlayerFactory.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no registered area!");

    Event event = area.event(eID);

    if (event == null)
      throw new ServiceException(area.name() + " has no event " + eID);
    if (!event.test(player))
      throw new ServiceException("You are not qualified to  "
          + event.description());

    try {
      event.trigger(player);
    } catch (LoadException l) {
      throw new ServiceException(l.getMessage());
    }
  }
}