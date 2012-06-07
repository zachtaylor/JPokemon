package jpkmn.game;

import jpkmn.game.pokemon.storage.Party;

public class Trainer {
  public Trainer() {
    party = new Party();
  }
  
  public Trainer(String name) {
    this();
    _name = name;
  }

  public String name() {
    return _name;
  }

  public void setName(String s) {
    _name = s;
  }

  public final Party party;
  protected String _name;
}
