package jpkmn.game.service;

import jpkmn.exceptions.LoadException;
import jpkmn.exceptions.ServiceException;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.AreaRegistry;
import jpkmn.map.Direction;
import jpkmn.map.Event;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerService {
  public static JSONObject loadPlayer(String name) throws ServiceException {
    Player p;

    try {
      p = PlayerRegistry.fromFile(name);
    } catch (LoadException e) {
      e.printStackTrace();
      throw new ServiceException(e.getMessage());
    }

    try {
      return JSONMaker.make(p);
    } catch (JSONException jsone) {
      jsone.printStackTrace();
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static JSONObject savePlayer(int playerID) throws ServiceException {
    try {
      PlayerRegistry.saveFile(playerID);
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }

    try {
      return JSONMaker.make(PlayerRegistry.get(playerID));
    } catch (JSONException e) {
      e.printStackTrace();
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static JSONObject pokemonInfo(int pID, int i) throws ServiceException {
    Player player = PlayerRegistry.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Pokemon pokemon = player.party.get(i);

    if (pokemon == null)
      throw new ServiceException(player.name() + " has no " + i + " Pokemon");

    try {
      return JSONMaker.make(pokemon);
    } catch (JSONException e) {
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static JSONObject areaInfo(int playerID) throws ServiceException {
    Player player = PlayerRegistry.get(playerID);

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
    Player player = PlayerRegistry.get(pID);

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
    Player player = PlayerRegistry.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no registered area!");

    Event event = null;
    for (Event cur : area.events())
      if (cur.id() == eID) event = cur;

    if (event == null)
      throw new ServiceException(area.name() + " has no event " + eID);
    if (!event.test(player))
      throw new ServiceException("You are not qualified to  "
          + event.description());

    event.trigger(player);
  }

  public static void attachGraphicsHandler(int playerID) {
    Player player = PlayerRegistry.get(playerID);

    player.screen.player(player);

    player.screen.showWorld();
  }
}