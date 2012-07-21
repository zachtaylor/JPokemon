package jpkmn.game.player;

import jpkmn.exe.gui.GraphicsHandler;
import jpkmn.game.pokemon.storage.Party;

public class AbstractPlayer {
  public final Party party;
  public final GraphicsHandler screen;

  public AbstractPlayer() {
    party = new Party(this);
    screen = new GraphicsHandler();
  }

  public String name() {
    return _name;
  }

  public void name(String s) {
    _name = s;
  }

  protected String _name;
}
