package jpkmn.game.service;

import jpkmn.exceptions.ServiceException;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.map.Area;
import jpkmn.map.AreaConnection;
import jpkmn.map.AreaManager;
import jpkmn.map.Direction;

import org.json.JSONException;
import org.json.JSONObject;

public class PlayerService {
  public static JSONObject areaInfo(int playerID) throws ServiceException {
    Player p = PlayerRegistry.get(playerID);
    Area area = AreaManager.get(p.area().id);

    try {
      return JSONMaker.make(area);
    } catch (JSONException e) {
      throw new ServiceException("There was an error. It's not your fault.");
    }
  }

  public static void areaChange(int playerID, String direction)
      throws ServiceException {
    Player p = PlayerRegistry.get(playerID);

    if (p == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    // HACKY - But i'm too tired to do it the right way right now
    Direction d = null;
    for (Direction dir : Direction.values())
      if (dir.toString().equals(direction)) d = dir;
    // END HACK

    Area cur = p.area();
    AreaConnection con = cur.neighbor(d);

    if (con == null)
      throw new ServiceException(p.area().name() + " has no " + d + " neighbor");
    if (!con.test(p))
      throw new ServiceException(p.name() + " cannot access " + d);

    con.use(p);
  }
}