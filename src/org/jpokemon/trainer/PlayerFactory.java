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

    if (fileMapping.values().contains(name)) {
      int n = 0;
      for (n = 0; fileMapping.values().contains(name + n); n++)
        ;
      name = name + n;
    }
    fileMapping.put(player.id(), name);

    player.name(name);
    player.add(new Pokemon(pokemonNumber, STARTER_POKEMON_LEVEL));

    return player;
  }

  public static Player load(String playername) throws LoadException {
    if (playername.endsWith(".jpkmn"))
      playername.substring(0, playername.length() - ".jpkmn".length());

    Player player = newPlayer();

    if (fileMapping.values().contains(playername))
      throw new LoadException("File already loaded: " + playername);

    File file = new File(SAVE_PATH + playername + ".jpkmn");

    if (!file.exists())
      throw new LoadException("Save file not found: " + playername);

    try {
      player.loadXML(XMLParser.parse(file).get(0));
    } catch (FileNotFoundException e) { // The file exists, but FNFE...
      e.printStackTrace();
    }

    return player;
  }

  public static void save(int playerID) {
    String path = SAVE_PATH + fileMapping.get(playerID) + ".jpkmn";

    try {
      Writer writer = new BufferedWriter(new PrintWriter(new File(path)));
      writer.write(PlayerFactory.get(playerID).toXML().toString());
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