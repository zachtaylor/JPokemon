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
import org.jpokemon.trainer.TrainerState;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerService {
  public static JSONObject pull(int playerID) {
    Player p = PlayerFactory.get(playerID);

    JSONObject response = new JSONObject();
    try {
      response.put("state", p.state().toString());
      response.put("player", p.toJSON(null));

      if (p.state() == TrainerState.BATTLE)
        response.put("battle", BattleService.info(playerID));
      else if (p.state() == TrainerState.UPGRADE)
        response.put("upgrade", p.toJSON(p.state()));

    } catch (Exception e) {
      e.printStackTrace();
      // throw new ServiceException(e.getMessage());
    }

    return response;
  }

  public static void load(String name) throws ServiceException {
    try {
      Player p = PlayerFactory.load(name);

      p.state(TrainerState.UPGRADE);
      p.notify("Foo", "Bar");
      p.notify("Hello world");
    } catch (LoadException e) {
      e.printStackTrace();
      throw new ServiceException(e.getMessage());
    }
  }

  public static void save(int playerID) throws ServiceException {
    try {
      PlayerFactory.save(playerID);
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }
  }

  public static void create(String name, String starter) throws ServiceException, JSONException {
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
      PlayerFactory.create(name, pokemon);
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
      throw new ServiceException("You are not qualified to  " + event.description());

    try {
      event.trigger(player);
    } catch (LoadException l) {
      throw new ServiceException(l.getMessage());
    }
  }
}