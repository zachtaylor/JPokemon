package jpkmn.game.service;

import jpkmn.exceptions.LoadException;
import jpkmn.exceptions.ServiceException;
import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.AreaRegistry;
import jpkmn.map.Direction;
import jpkmn.map.Event;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.stat.StatType;
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

  public static void party(JSONObject request) {
    try {
      Player player = PlayerFactory.get(request.getInt("id"));

      if (player.state() == TrainerState.OVERWORLD) {
        player.state(TrainerState.UPGRADE);
      }
      else if (player.state() == TrainerState.UPGRADE) {
        if (request.get("stats") == JSONObject.NULL) {
          player.state(TrainerState.OVERWORLD);
          return;
        }

        Pokemon pokemon = player.party().get(request.getInt("index"));

        JSONArray stats = request.getJSONArray("stats");
        for (int i = 0; i < stats.length(); i++) {
          StatType s = StatType.valueOf(stats.getJSONObject(i).getString("stat"));
          pokemon.statPoints(s, stats.getJSONObject(i).getInt("amount"));
        }
      }
      else
        ; // TODO throw error

    } catch (JSONException e) {
    }
  }

  // ------------------------------------------------------------
  // THE ONES BELOW THIS LINE SUCK AND WILL DISSAPPEAR EVENTUALLY
  // ------------------------------------------------------------

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