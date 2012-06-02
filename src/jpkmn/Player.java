package jpkmn;


import java.io.PrintWriter;
import java.util.*;

import javax.swing.JOptionPane;

import jpkmn.gui.Graphics;
import jpkmn.item.*;
import jpkmn.pokemon.*;

public class Player {
  public final Bag bag;
  public final StorageBox box;
  public final Party party;
  public final Pokedex dex;

  public Player(String serial) {
    Player.serial = serial;
    dex = new Pokedex();
    bag = new Bag();
    box = new StorageBox();
    party = new Party();
  }

  public void createNew() {
    name = JOptionPane.showInputDialog(null,
        "Welcome, new player.\nPlease enter your name", "New Player",
        JOptionPane.QUESTION_MESSAGE);

    Party starters = new Party();
    starters.add(new Pokemon(1, 5));
    starters.add(new Pokemon(4, 5));
    starters.add(new Pokemon(7, 5));
    int pos = jpkmn.gui.Tools.selectFromParty("Select your starter!", starters);
    if (pos == -1) return;

    party.add(starters.get(pos));
  }

  public String name() {
    return name;
  }

  public int cash() {
    return cash;
  }

  public void addCash(int change) {
    cash += change;
  }

  public int badges() {
    return badge;
  }

  public int addBadge() {
    return ++badge;
  }

  public void alert(Object o, String... s) {
    graphics.alert(o, s);
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
    dex.toFile(p);
  }

  public static Player fromFile(Scanner s) {
    Player p = new Player(s.nextLine());
    p.name = s.nextLine();
    p.party.readFile(s);

    p.cash = s.nextInt();
    p.badge = s.nextInt();
    p.bag.fromFile(s);
    s.nextLine();
    while (s.hasNext() && s.next().equals("|")) {
      p.box.add(Pokemon.fromFile(s));
    }

    p.dex.readFile(s);

    return p;
  }

  public static boolean isCurrent() {
    return serial.equals(Driver.officialSerial);
  }

  private String name;
  private int cash, badge;
  private static String serial;
  private Graphics graphics;
}