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

public class PlayerRegistry {
  private int a;

  public static int UNIQUE_ID;

  public static Player createNew() {
    try {
      Player newPlayer = new Player(Driver.officialSerial);

      // TODO Name the new player

      // TODO Ask the new player for a starter pokemon

      register(newPlayer);
      return newPlayer;

    } catch (LoadException l) {
      // Something failed...
    }

    return null;
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
      newPlayer.setCash(Integer.parseInt(scan.nextLine()));
      newPlayer.setBadge(Integer.parseInt(scan.nextLine()));

      for (int i = 0; i < Constants.PARTYSIZE; i++)
        newPlayer.party.add(Pokemon.createFromString(scan.nextLine()));

      newPlayer.bag.fromFile(scan);
      newPlayer.dex.readSeen(scan.nextLine());
      newPlayer.dex.readOwn(scan.nextLine());

      while (scan.hasNext())
        newPlayer.box.add(Pokemon.createFromString(scan.nextLine()));

      register(newPlayer);
      return newPlayer;

    } catch (FileNotFoundException f) {
      throw new LoadException("That player does not exist.");
    }
  }

  private static void register(Player p) throws LoadException {
    if (names == null)
      names = new ArrayList<String>();

    else if (names.contains(p.name()))
      throw new LoadException("Player is already registered: " + p.name());

    p._id = UNIQUE_ID++;
    names.add(p.name());
  }

  private static List<String> names;
}
