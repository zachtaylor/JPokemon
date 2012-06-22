package jpkmn.game.pokemon.storage;

import jpkmn.Constants;
import jpkmn.game.PokemonTrainer;
import jpkmn.game.pokemon.Pokemon;

public class Party extends AbstractParty {
  public Party(PokemonTrainer pt) {
    super();
    _owner = pt;
  }

  public boolean add(Pokemon p) {
    if (p == null || _amount == Constants.PARTYSIZE || contains(p))
      return false;

    _data[_amount++] = p;
    p.setOwner(_owner);
    
    return true;
  }

  public boolean remove(int index) {
    if (index < 0) return false;

    for (int i = index; i < _amount - 1; i++)
      _data[i] = _data[i + 1];

    _data[--_amount].setOwner(null);
    _data[_amount] = null;

    return true;
  }

  private PokemonTrainer _owner;
}
