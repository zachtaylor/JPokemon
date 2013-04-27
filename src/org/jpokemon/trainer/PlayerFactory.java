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
import org.jpokemon.service.LoadException;
import org.zachtaylor.jnodalxml.XMLParser;

public class PlayerFactory {
  public static Player get(int id) {
    if (players.get(id) == null)
      throw new IllegalArgumentException("Could not retrieve PlayerID: " + id);

    return players.get(id);
  }

  public static Player create(String name) {
    name = getUniqueName(name);
    Player player = newPlayer();
    player.name(name);
    fileMapping.put(player, name);

    return player;
  }

  public static Player load(String filename) throws LoadException {
    if (fileMapping.values().contains(filename))
      throw new LoadException("File already loaded");

    File file = new File(JPokemonConstants.SAVE_PATH + filename + ".jpkmn");

    if (!file.exists())
      throw new LoadException("Save file not found");

    Player player = newPlayer();

    try {
      player.loadXML(XMLParser.parse(file).get(0));
    } catch (FileNotFoundException e) {
    }

    fileMapping.put(player, filename);

    return player;
  }

  public static void save(Player player) {
    String path = JPokemonConstants.SAVE_PATH + fileMapping.get(player) + ".jpkmn";

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

  private static String getUniqueName(String attempt) {
    if (!fileMapping.containsValue(attempt) && !new File(JPokemonConstants.SAVE_PATH + attempt + ".jpkmn").exists())
      return attempt;

    int n = 0;
    for (; fileMapping.containsValue(attempt + n) || new File(JPokemonConstants.SAVE_PATH + attempt + n + ".jpkmn").exists(); n++)
      ;

    return attempt + n;
  }

  private static Map<Integer, Player> players = new HashMap<Integer, Player>();
  private static Map<Player, String> fileMapping = new HashMap<Player, String>();
}