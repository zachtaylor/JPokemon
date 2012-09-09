package jpkmn.game.service;

import jpkmn.exceptions.ServiceException;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.Direction;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerService {
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

    Area area = player.area();

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

    Area area = player.area();

    if (area == null)
      throw new ServiceException(player.name() + " has no registered area!");

    AreaConnection connection = area.neighbor(direction);

    if (connection == null)
      throw new ServiceException(area.name() + " cannot access " + direction);
    if (!connection.test(player))
      throw new ServiceException("You are not qualified to go  " + direction);

    connection.use(player);
  }
}