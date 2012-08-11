package jpkmn.game.pokemon.move;

import java.util.ArrayList;
import java.util.List;

import jpkmn.Constants;
import jpkmn.exceptions.CancelException;
import jpkmn.game.base.MoveMap;
import jpkmn.game.pokemon.Pokemon;

public class MoveBlock {
  public MoveBlock(Pokemon p) {
    pkmn = p;
    moves = new Move[Constants.MOVENUMBER];

    setDefaults();
  }

  public int amount() {
    return amount;
  }

  public Move get(int i) {
    if (i > amount || i < 0) return null;
    return moves[i];
  }

  public void restoreAll() {
    for (int i = 0; i < amount; i++) {
      if (moves[i] != null) moves[i].restore();
    }
  }

  public String[] list() {
    List<String> response = new ArrayList<String>();

    for (Move m : moves) {
      if (m != null)
        response.add(m.name() + " (" + m.ppcur() + "/" + m.ppmax() + ")");
    }

    return response.toArray(new String[amount]);
  }

  public void check() {
    MoveMap m = MoveMap.getMapForPokemonNumberAtLevel(pkmn.number(),
        pkmn.level());
    if (m == null) return;

    add(m.getMove_number());
  }

  public boolean add(int number) {
    int position;

    if (amount < moves.length) {
      position = amount++;
    }
    else {
      try {
        position = pkmn.owner().screen.getMoveIndex("replace", pkmn);
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
      moves[position] = new Move(number, pkmn);
      return true;
    }
    else
      return false;
  }

  public void removeAll() {
    for (int i = 0; i < Constants.MOVENUMBER; i++) {
      moves[i] = null;
    }
    amount = 0;
  }

  /**
   * Picks up to 4 moves randomly from the list of moves that this Pokemon
   * could have learned by this level, and assigns them.
   */
  private void setDefaults() {
    ArrayList<Integer> possible = new ArrayList<Integer>();

    for (int l = 1; l <= pkmn.level(); l++) {
      MoveMap m = MoveMap.getMapForPokemonNumberAtLevel(pkmn.number(), l);

      if (m != null && !possible.contains(m)) possible.add(m.getMove_number());
    }

    while (!possible.isEmpty() && amount < moves.length)
      add(possible.remove((int) (Math.random() * possible.size())), amount++);
  }

  private boolean contains(int number) {
    for (Move m : moves)
      if (m != null && m.number() == number) return true;
    return false;
  }

  private int amount;
  private Move[] moves;
  private Pokemon pkmn;
}