package jpkmn.game.player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.JPokemonConstants;

public class PlayerRegistry {
  public static Player get(int playerID) {
    return _players.get(playerID);
  }

  public static Player create(String name, int start) throws LoadException {
    Player newPlayer = newPlayer();
    newPlayer.name(name);
    newPlayer.party.add(new Pokemon(start, 5));
    return newPlayer;
  }

  public static Player fromFile(String s) throws LoadException {
    if (!s.endsWith(".jpkmn"))
      s += ".jpkmn";

    try {
      File playerFile = new File(JPokemonConstants.SAVE_DIR + s);
      Scanner scan = new Scanner(playerFile);

      return newPlayer().load(scan);
    } catch (FileNotFoundException f) {
      return null;
    }
  }

  public static void saveFile(int playerID) throws LoadException {
    Player player = get(playerID);

    if (player == null)
      throw new LoadException("Could not load player: " + playerID);

    String path = player.name() + ".jpkmn";

    try {
      File file = new File(JPokemonConstants.SAVE_DIR + path);
      if (!file.exists())
        file.createNewFile();

      PrintWriter writer = new PrintWriter(file);

      writer.write(player.save());
      writer.close();
    } catch (Exception e) {
      throw new LoadException(e.getMessage());
    }
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