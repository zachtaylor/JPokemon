package jpkmn.game.pokemon.move;

import java.util.ArrayList;
import java.util.List;

import jpkmn.Constants;
import jpkmn.exceptions.CancelException;
import jpkmn.game.base.MoveMap;
import jpkmn.game.pokemon.Pokemon;

public class MoveBlock {
  public MoveBlock(Pokemon p) {
    _pokemon = p;
    moves = new Move[Constants.MOVENUMBER];

    ArrayList<Integer> possible = new ArrayList<Integer>();

    for (int l = 1; l <= _pokemon.level(); l++) {
      MoveMap m = MoveMap.getMapForPokemonNumberAtLevel(_pokemon.number(), l);

      if (m != null && !possible.contains(m)) possible.add(m.getMove_number());
    }

    while (!possible.isEmpty() && _amount < moves.length)
      add(possible.remove((int) (Math.random() * possible.size())), _amount++);
  }

  public int amount() {
    return _amount;
  }

  public Move get(int i) {
    if (i < 0 || i >= _amount) return null;
    return moves[i];
  }

  public boolean add(int number) {
    int position;

    if (_amount < moves.length)
      position = _amount++;
    else {
      try {
        position = _pokemon.owner().screen.getMoveIndex("replace", _pokemon);
      } catch (CancelException c) {
        return false;
      }
    }

    if (position > -1)
      return add(number, position);
    else
      return false;
  }

  public boolean add(int number, int position) {
    if (!contains(number)) {
      moves[position] = new Move(number, _pokemon);
      return true;
    }
    else
      return false;
  }

  public void restoreAll() {
    for (int i = 0; i < _amount; i++) {
      if (moves[i] != null) moves[i].pp(moves[i].ppmax());
    }
  }

  public void removeAll() {
    for (int i = 0; i < Constants.MOVENUMBER; i++)
      moves[i] = null;

    _amount = 0;
  }

  public String[] list() {
    List<String> response = new ArrayList<String>();

    for (int moveIndex = 0; moveIndex < _amount; moveIndex++)
      response.add(moves[moveIndex].toString());

    return response.toArray(new String[_amount]);
  }

  public void check() {
    MoveMap m = MoveMap.getMapForPokemonNumberAtLevel(_pokemon.number(),
        _pokemon.level());
    if (m == null) return;

    add(m.getMove_number());
  }

  private boolean contains(int number) {
    for (Move m : moves)
      if (m != null && m.number() == number) return true;
    return false;
  }

  private int _amount;
  private Move[] moves;
  private Pokemon _pokemon;
}