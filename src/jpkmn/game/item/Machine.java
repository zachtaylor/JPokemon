package jpkmn.game.item;

import jpkmn.game.base.MoveBase;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;

public class Machine extends Item {

  public Machine(double move, int value) {
    super("TM-" + MoveBase.getBaseForNumber((int) move).getName(), value);
    _move = (int) move;
  }

  @Override
  public boolean effect(Pokemon p) {
    Move m = new Move(_move, p);

    if ((p.type1() == m.type() || p.type2() == m.type()) && !reduce())
      return p.moves.add(_move);

    return false;
  }

  private int _move;
}
