package jpkmn;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.prefs.Preferences;

import exceptions.LoadException;
import exe.Driver;

import jpkmn.pokemon.Pokemon;

public class PlayerFactory {
  public static Player createNew() {
    try {
      Player newPlayer = new Player(Driver.officialSerial);

      // TODO Name the new player

      // TODO Ask the new player for a starter pokemon

      register(newPlayer);
      return newPlayer;

    } catch (LoadException l) {
      // TODO Invalid file format
    }

    return null;
  }

  public static Player fromFile(String s) {
    try {
      Preferences pref = Preferences.systemNodeForPackage(PlayerFactory.class);
      File playerFile = new File(pref.get("save_dir", "save") + "/" + s);
      Scanner scan = new Scanner(playerFile);

      Player newPlayer = new Player(scan.nextLine());
      newPlayer.setName(scan.nextLine());
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
      // TODO That player doesn't exist on the system
    } catch (LoadException l) {
      l.printStackTrace();
      System.out.println("\n" + l.getMessage());
    }

    return null;
  }

  private static void register(Player p) throws LoadException {
    if (names == null)
      names = new ArrayList<String>();

    else if (names.contains(p.name()))
      throw new LoadException("Player is already registered: " + p.name());

    names.add(p.name());
  }

  private static List<String> names;
}
