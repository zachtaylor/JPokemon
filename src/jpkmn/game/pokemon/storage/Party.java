package jpkmn.game.pokemon.storage;

import jpkmn.Constants;
import jpkmn.game.Player;
import jpkmn.game.pokemon.Pokemon;

public class Party extends AbstractParty {
  public Party(Player p) {
    super();
    _owner = p;
  }

  public boolean add(Pokemon p) {
    if (p == null || _amount == Constants.PARTYSIZE || contains(p))
      return false;

    _data[_amount++] = p;
    p.owner(_owner);
    
    return true;
  }

  public boolean remove(int index) {
    if (index < 0) return false;

    for (int i = index; i < _amount - 1; i++)
      _data[i] = _data[i + 1];

    _data[--_amount].owner(null);
    _data[_amount] = null;

    return true;
  }

  private Player _owner;
}
