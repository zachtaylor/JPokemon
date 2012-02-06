package jpkmn;

import gui.Splash;
import item.*;
import pokemon.*;

import java.io.PrintWriter;
import java.util.*;

import javax.swing.JOptionPane;

public class Player {
  public int cash, badge;
  public String name;
  public Bag bag;
  public ArrayList<Pokemon> box = new ArrayList<Pokemon>();
  public Party party;
  private static String serial;

  public Player(String serial) {
    Player.serial = serial;
  }


  public void createNew() {
    name = JOptionPane.showInputDialog(null,
        "Welcome, new player.\nPlease enter your name", "New Player",
        JOptionPane.QUESTION_MESSAGE);

    Party starters = new Party();
    starters.add(new Pokemon(1, 5));
    starters.add(new Pokemon(4, 5));
    starters.add(new Pokemon(7, 5));
    int pos = gui.Tools.selectStarter(starters);
    if (pos == -1)
      return;

    party = new Party();
    box = new ArrayList<Pokemon>();
    bag = new Bag();
    party.add(starters.pkmn[pos]);
  }

  public void changeCash(int change) {
    cash += change;

    // TODO: notify
  }

  public void getBadge() {
    badge++;

    // TODO: notify

  }

  public void toFile(PrintWriter p) {
    p.println(serial);
    p.println(name);
    party.toFile(p);
    p.println(cash + " " + badge);
    bag.toFile(p);
    p.println();
    for (Pokemon pkmn : box) {
      pkmn.toFile(p);
    }
  }

  public static Player fromFile(Scanner s) {
    if (!isCurrent(s.next())) {
      Splash.showFatalErrorMessage("Incorrect file version");
      return null;
    }
    else
      s.nextLine();

    Player p = new Player(s.nextLine());
    p.party = new Party();
    p.party.fromFile(s);

    p.cash = s.nextInt();
    p.badge = s.nextInt();
    p.bag = new Bag();
    p.bag.fromFile(s);
    s.nextLine();
    while (s.hasNext() && s.next().equals("|")) {
      p.box.add(Pokemon.fromFile(s));
    }

    return p;
  }

  public static boolean isCurrent(String test) {
    return test.equals(serial);
  }

}
