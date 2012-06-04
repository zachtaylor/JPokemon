package jpkmn.game.item;

import lib.MoveBase;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;

public class Machine extends Item {

  public Machine(int move, int quantity) {
    super(move, quantity, "TM-" + MoveBase.getBaseForNumber(move).getName());
  }

  @Override
  public boolean effect(Pokemon p) {
    Move m = new Move(getPower(), p);

    if (p.type1() != m.type() && p.type2() != m.type() && !reduce())
      return false;

    return p.moves.add(getPower());
  }
}
