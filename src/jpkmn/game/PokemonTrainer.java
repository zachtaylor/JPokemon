package jpkmn.game;

import jpkmn.game.pokemon.storage.Party;

public abstract class PokemonTrainer {
  public PokemonTrainer() {
    party = new Party(this);
  }
  
  public PokemonTrainer(String name) {
    this();
    setName(name);
  }

  public String name() {
    return _name;
  }

  public void setName(String s) {
    _name = s;
  }
  
  public abstract void notify(String... s);
  
  public final Party party;
  protected String _name;
}
