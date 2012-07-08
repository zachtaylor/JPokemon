package jpkmn.game;

import java.io.PrintWriter;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;
import jpkmn.exe.Driver;
import jpkmn.exe.gui.GraphicsHandler;
import jpkmn.game.item.Bag;
import jpkmn.game.pokemon.Pokedex;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.storage.PCStorage;
import jpkmn.game.pokemon.storage.Party;

public class Player {
  public final Bag bag;
  public final Pokedex dex;
  public final Party party;
  public final PCStorage box;
  public final GraphicsHandler screen;

  public Player(String serial) throws LoadException {
    if (!serial.equals(Driver.officialSerial))
      throw new LoadException("Improper file version: " + _serial);

    _serial = serial;

    bag = new Bag();
    dex = new Pokedex();
    box = new PCStorage();
    party = new Party(this);
    screen = new GraphicsHandler(this);
  }

  public String name() {
    return _name;
  }

  public void name(String s) {
    _name = s;
  }

  public int cash() {
    return _cash;
  }

  public void addCash(int change) {
    _cash += change;
  }

  public void setCash(int c) {
    _cash = c;
  }

  public int badges() {
    return _badge;
  }

  public int addBadge() {
    return ++_badge;
  }

  public void setBadge(int b) {
    _badge = b;
  }

  public void toFile(PrintWriter p) {
    p.println(_serial);
    p.println(_name);
    p.println(_cash);
    p.println(_badge);

    for (int i = 0; i < Constants.PARTYSIZE; i++)
      if (party.get(i) != null)
        p.println(party.get(i).saveToString());
      else
        p.println("||");

    p.println(bag.saveToString());

    p.println(dex.saveSeenToString());
    p.println(dex.saveOwnToString());

    for (Pokemon pkmn : box)
      p.println(pkmn.saveToString());
  }

  public boolean equals(Object o) {
    if (!(o instanceof Player)) return false;
    return ((Player) o)._id == _id;
  }

  public void notify(String... s) {
    screen.notify(s);
  }

  int _id;
  private int _cash, _badge;
  private String _serial, _name;
}