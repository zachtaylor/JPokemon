package jpkmn.game;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.Preferences;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;
import jpkmn.exe.Driver;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.AreaManager;

public class PlayerRegistry {
  public static int UNIQUE_ID;

  public static Player create(String name, int start) throws LoadException {
    Player newPlayer = new Player(Driver.officialSerial);
    newPlayer.name(name);
    newPlayer.party.add(new Pokemon(start, 5));
    return register(newPlayer);
  }

  public static Player fromFile(String s) throws LoadException {
    if (!s.endsWith(".jpkmn")) {
      s += ".jpkmn";
    }

    try {
      Preferences pref = Constants.prefs;
      File playerFile = new File(pref.get("save_dir", "save") + "/" + s);
      Scanner scan = new Scanner(playerFile);

      Player newPlayer = new Player(scan.nextLine());
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
    }
  }

  private static Player register(Player p) throws LoadException {
    if (names == null)
      names = new ArrayList<String>();
    else if (names.contains(p.name()))
      throw new LoadException("Player is already registered: " + p.name());

    p._id = UNIQUE_ID++;
    names.add(p.name());

    return p;
  }

  private static List<String> names;
}
