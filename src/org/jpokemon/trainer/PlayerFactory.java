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
  public static Player get(String id) {
    if (players.get(id) == null)
      throw new IllegalArgumentException("Could not retrieve PlayerID: " + id);

    return players.get(id);
  }

  public static Player create(String name, String rivalName) {
    Player player = newPlayer(name = getUniqueName(name));
    player.name(name);
    player.record().setRivalName(rivalName);

    return player;
  }

  public static Player load(String filename) throws LoadException {
    if (players.keySet().contains(filename))
      throw new LoadException("File already loaded");

    File file = new File(JPokemonConstants.SAVE_PATH + filename + ".jpkmn");

    if (!file.exists())
      throw new LoadException("Save file not found");

    Player player = newPlayer(filename);

    try {
      player.loadXML(XMLParser.parse(file).get(0));
    } catch (FileNotFoundException e) {
    }

    return player;
  }

  public static void save(Player player) {
    String path = JPokemonConstants.SAVE_PATH + player.id() + ".jpkmn";

    try {
      Writer writer = new BufferedWriter(new PrintWriter(new File(path)));
      writer.write(player.toXML().toString());
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static Player newPlayer(String id) {
    Player player = new Player(id);
    players.put(id, player);

    return player;
  }

  private static String getUniqueName(String attempt) {
    if (!players.containsKey(attempt) && !new File(JPokemonConstants.SAVE_PATH + attempt + ".jpkmn").exists())
      return attempt;

    int n = 0;
    for (; players.containsKey(attempt + n) || new File(JPokemonConstants.SAVE_PATH + attempt + n + ".jpkmn").exists(); n++)
      ;

    return attempt + n;
  }

  private static Map<String, Player> players = new HashMap<String, Player>();
}