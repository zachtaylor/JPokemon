package jpkmn.game.player;

import java.io.PrintWriter;

import jpkmn.Constants;
import jpkmn.game.item.Bag;
import jpkmn.game.pokemon.Pokedex;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.storage.PCStorage;
import jpkmn.map.Area;

public class Player extends AbstractPlayer {
  public final Bag bag;
  public final Pokedex dex;
  public final PCStorage box;

  public Player() {
    bag = new Bag();
    dex = new Pokedex();
    box = new PCStorage();

    screen.player(this);
  }

  public Area area() {
    return _area;
  }

  public void area(Area a) {
    _area = a;
  }

  public int badge() {
    return _badge;
  }

  public void badge(int b) {
    _badge = b;
  }

  public void toFile(PrintWriter p) {
    p.println(_name);
    p.println(_cash);
    p.println(_badge);
    p.println(_area.id);

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

  private Area _area;
  private int _badge;
}