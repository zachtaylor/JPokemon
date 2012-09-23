package jpkmn.game.player;

import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.exceptions.LoadException;
import jpkmn.game.item.Bag;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.storage.PCStorage;

public class Player extends Trainer {
  public final Bag bag;
  public final Pokedex dex;
  public final PCStorage box;

  public Player(int playerID) {
    super();

    _id = playerID;
    _area = -1;
    _badge = -1;

    bag = new Bag();
    dex = new Pokedex();
    box = new PCStorage();
  }

  public int area() {
    return _area;
  }

  public void area(int a) {
    _area = a;
  }

  public int badge() {
    return _badge;
  }

  public void badge(int b) {
    _badge = b;
  }

  public String save() {
    StringBuilder data = new StringBuilder();

    data.append(name());
    data.append("\n");
    data.append(cash());
    data.append(" ");
    data.append(badge());
    data.append(" ");
    data.append(area());
    data.append("\n");

    for (int partyIndex = 0; partyIndex < Constants.PARTYSIZE; ++partyIndex) {
      if (party.get(partyIndex) != null)
        data.append(party.get(partyIndex).save());
      else
        data.append(" \n");
    }

    // save bag
    data.append(bag.save());

    // save pokedex
    data.append(dex.save());

    // save pcstorage
    for (Pokemon p : box)
      data.append(p.save());

    return data.toString();
  }

  public Player load(Scanner scan) throws LoadException {
    try {
      name(scan.nextLine());
      cash(scan.nextInt());
      badge(scan.nextInt());
      area(Integer.parseInt(scan.nextLine().trim()));

      // Load party
      for (int partyIndex = 0; partyIndex < Constants.PARTYSIZE; ++partyIndex)
        party.add(Pokemon.load(scan.nextLine()));

      // Load bag
      bag.load(scan.nextLine());

      // Load pokedex
      dex.load(scan.nextLine());

      // load pcstorage
      while (scan.hasNextLine())
        box.add(Pokemon.load(scan.nextLine()));

      return this;
    } catch (LoadException le) {
      throw le;
    } catch (Exception e) {
      throw new LoadException("Player could not load ");
    }
  }

  public boolean equals(Object o) {
    if (!(o instanceof Player)) return false;
    return ((Player) o)._id == _id;
  }

  public int hashCode() {
    return _id;
  }

  private int _area, _badge;
}