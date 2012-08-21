package jpkmn.game.player;

import jpkmn.game.item.Bag;
import jpkmn.game.pokemon.Pokedex;
import jpkmn.game.pokemon.storage.PCStorage;
import jpkmn.map.Area;

public class Player extends AbstractPlayer {
  public final Bag bag;
  public final Pokedex dex;
  public final PCStorage box;

  public Player(int playerID) {
    _id = playerID;
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

  public boolean equals(Object o) {
    if (!(o instanceof Player)) return false;
    return ((Player) o)._id == _id;
  }

  private Area _area;
  private int _badge;
}