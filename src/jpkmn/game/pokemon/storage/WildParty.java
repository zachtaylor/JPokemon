package jpkmn.game.pokemon.storage;

import jpkmn.Constants;
import jpkmn.game.pokemon.Pokemon;

public class WildParty extends AbstractParty {

  @Override
  public boolean add(Pokemon p) {
    if (p == null || _amount == Constants.PARTYSIZE || contains(p))
      return false;

    _data[_amount++] = p;
    
    return true;
  }

  @Override
  public boolean remove(int index) {
    if (index < 0) return false;

    for (int i = index; i < _amount - 1; i++)
      _data[i] = _data[i + 1];

    _data[_amount] = null;

    return true;
  }

}
