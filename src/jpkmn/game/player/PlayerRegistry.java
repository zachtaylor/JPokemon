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

public class PlayerRegistry implements JPokemonConstants {
  public static Player get(int id) {
    if (players.get(id) == null)
      throw new IllegalArgumentException("Could not retrieve PlayerID: " + id);

    return players.get(id);
  }

  public static Player start(String name, int pokemonNumber) {
    Player player = newPlayer();

    player.name(name);
    player.add(new Pokemon(pokemonNumber, STARTER_POKEMON_LEVEL));

    String filePath = name;

    for (int n = 0; fileMapping.values().contains(filePath); n++)
      filePath = name + n;
    fileMapping.put(player.id(), filePath);

    return player;
  }

  public static Player load(String filepath) throws LoadException {
    Player player = newPlayer();

    if (fileMapping.values().contains(filepath))
      throw new LoadException("File already in use: " + filepath);

    try {
      fileMapping.put(player.id(), filepath);
      player.load(new Scanner(new File(SAVE_PATH + filepath)));
    } catch (FileNotFoundException f) {
      throw new LoadException(f.getMessage());
    }

    return player;
  }

  public static void save(int playerID) throws LoadException {
    String path = fileMapping.get(playerID);

    try {
      PrintWriter writer = new PrintWriter(new File(SAVE_PATH + path));

      players.get(playerID).save(writer);
      writer.close();
    } catch (FileNotFoundException e) {
      throw new LoadException("Cannot access file: " + path);
    }
  }

  private static Player newPlayer() {
    Player player = new Player();
    players.put(player.id(), player);

    return player;
  }

  private static Map<Integer, Player> players = new HashMap<Integer, Player>();
  private static Map<Integer, String> fileMapping = new HashMap<Integer, String>();
}