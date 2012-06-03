package jpkmn;

import java.io.PrintWriter;

import exceptions.LoadException;

import exe.Driver;

import jpkmn.item.Bag;
import jpkmn.pokemon.Pokedex;
import jpkmn.pokemon.Pokemon;
import jpkmn.pokemon.storage.Party;
import jpkmn.pokemon.storage.PCStorage;

public class Player {
  public final Bag bag;
  public final PCStorage box;
  public final Party party;
  public final Pokedex dex;

  public Player(String serial) throws LoadException {
    if (!serial.equals(Driver.officialSerial))
      throw new LoadException("Improper file version: " + _serial);

    _serial = serial;
    dex = new Pokedex();
    bag = new Bag();
    box = new PCStorage();
    party = new Party();
  }

  public String name() {
    return _name;
  }

  public void setName(String s) {
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

  private String _name;
  private int _cash, _badge;
  private String _serial;
}