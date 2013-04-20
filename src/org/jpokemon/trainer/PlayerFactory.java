package org.jpokemon.trainer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.service.LoadException;

import com.zachtaylor.jnodalxml.XMLParser;

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

    if (fileMapping.values().contains(name)) {
      int n = 0;
      for (n = 0; fileMapping.values().contains(name + n); n++)
        ;
      name = name + n;
    }
    fileMapping.put(player, name);

    return player;
  }

  public static Player load(String filename) throws LoadException {
    Player player = newPlayer();

    if (fileMapping.values().contains(filename))
      throw new LoadException("File already loaded: " + filename);

    File file = new File(SAVE_PATH + filename + ".jpkmn");

    try {
      player.loadXML(XMLParser.parse(file).get(0));
    } catch (FileNotFoundException e) { // The file exists, but FNFE...
      throw new LoadException("Save file not found: " + filename);
    }

    return player;
  }

  public static void save(Player player) {
    String path = SAVE_PATH + fileMapping.get(player) + ".jpkmn";

    try {
      Writer writer = new BufferedWriter(new PrintWriter(new File(path)));
      writer.write(player.toXML().toString());
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
  private static Map<Player, String> fileMapping = new HashMap<Player, String>();
}