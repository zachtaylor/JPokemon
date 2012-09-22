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

public class PlayerRegistry {
  public static Player create(String name, int start) throws LoadException {
    Player newPlayer = newPlayer();
    newPlayer.name(name);
    newPlayer.party.add(new Pokemon(start, 5));
    return newPlayer;
  }

  public static Player fromFile(String s) throws LoadException {
    if (!s.endsWith(".jpkmn")) s += ".jpkmn";

    try {
      Preferences pref = Constants.prefs;
      File playerFile = new File(pref.get("save_dir", "save") + "/" + s);
      Scanner scan = new Scanner(playerFile);

      return newPlayer().load(scan);
    } catch (FileNotFoundException f) {
      throw new LoadException("That player does not exist.");
    } catch (Exception e) {
      throw new LoadException("General error - " + e.toString());
    }
  }

  public static Player get(int playerID) {
    return _players.get(playerID);
  }

  private static Player newPlayer() {
    // Eventually synchronize
    Player player = new Player(PLAYER_COUNT++);
    _players.put(player.id(), player);
    return player;
  }

  private static int PLAYER_COUNT; // Eventually move to DB
  private static Map<Integer, Player> _players = new HashMap<Integer, Player>();
}