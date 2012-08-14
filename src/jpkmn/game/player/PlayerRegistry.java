package jpkmn.game.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.prefs.Preferences;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.AreaManager;

public class PlayerRegistry {
  public static Player create(String name, int start) throws LoadException {
    Player newPlayer = new Player(PLAYER_COUNT++);
    newPlayer.name(name);
    newPlayer.party.add(new Pokemon(start, 5));
    return register(newPlayer);
  }

  public static Player fromFile(String s) throws LoadException {
    if (!s.endsWith(".jpkmn")) s += ".jpkmn";

    try {
      Preferences pref = Constants.prefs;
      File playerFile = new File(pref.get("save_dir", "save") + "/" + s);
      Scanner scan = new Scanner(playerFile);

      Player newPlayer = new Player(PLAYER_COUNT++);
      newPlayer.name(scan.nextLine());
      newPlayer.cash(Integer.parseInt(scan.nextLine()));
      newPlayer.badge(Integer.parseInt(scan.nextLine()));
      newPlayer.area(AreaManager.get(Integer.parseInt(scan.nextLine())));

      for (int i = 0; i < Constants.PARTYSIZE; i++)
        newPlayer.party.add(Pokemon.createFromString(scan.nextLine()));

      newPlayer.bag.fromFile(scan);
      newPlayer.dex.readSeen(scan.nextLine());
      newPlayer.dex.readOwn(scan.nextLine());

      while (scan.hasNext())
        newPlayer.box.add(Pokemon.createFromString(scan.nextLine()));

      return register(newPlayer);
    } catch (FileNotFoundException f) {
      throw new LoadException("That player does not exist.");
    } catch (Exception e) {
      throw new LoadException("General error - " + e.toString());
    }
  }

  public static Player get(int playerID) {
    return _players.get(playerID);
  }

  private static Player register(Player p) throws LoadException {
    if (_players.get(p._id) != null) {
      Player player = _players.get(p._id);

      throw new LoadException("PlayerID " + p._id + " is already assigned to "
          + player.name());
    }

    return p;
  }

  private static int PLAYER_COUNT; // Eventually move to DB
  private static Map<Integer, Player> _players = new HashMap<Integer, Player>();
}