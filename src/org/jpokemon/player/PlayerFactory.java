package org.jpokemon.player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jpkmn.exceptions.LoadException;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.player.Player;

public class PlayerFactory implements JPokemonConstants {
  public static Player get(int id) {
    if (players.get(id) == null)
      throw new IllegalArgumentException("Could not retrieve PlayerID: " + id);

    return players.get(id);
  }

  public static Player create(String name, int pokemonNumber) {
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
    String path = SAVE_PATH + fileMapping.get(playerID);

    try {
      Writer writer = new BufferedWriter(new PrintWriter(new File(path)));
      writer.write(PlayerFactory.get(playerID).toJSON().toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
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