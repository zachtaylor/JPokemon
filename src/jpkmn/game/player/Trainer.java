package jpkmn.game.player;

import jpkmn.game.pokemon.storage.Party;
import jpkmn.game.service.GraphicsHandler;

public abstract class Trainer {
  public final Party party;
  public final GraphicsHandler screen;

  public Trainer() {
    party = new Party(this);
    screen = new GraphicsHandler();
  }

  public int id() {
    return _id;
  }

  public int cash() {
    return _cash;
  }

  public void cash(int c) {
    _cash = c;
  }

  public String name() {
    return _name;
  }

  public void name(String s) {
    _name = s;
  }

  protected String _name;
  protected int _id, _cash;
}