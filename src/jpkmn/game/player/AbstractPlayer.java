package jpkmn.game.player;

import jpkmn.game.pokemon.storage.Party;
import jpkmn.game.service.GraphicsHandler;

public class AbstractPlayer {
  public final Party party;
  public final GraphicsHandler screen;

  public AbstractPlayer() {
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

  protected int _cash, _id = -1;
  protected String _name;
}