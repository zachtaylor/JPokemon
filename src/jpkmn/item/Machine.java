package jpkmn.item;

import lib.MoveBase;

import jpkmn.pokemon.Pokemon;
import jpkmn.pokemon.move.Move;

public class Machine extends Item {
  private int a; // Flag to do work

  public Machine(int move, int quantity) {
    super(move, quantity, "TM-" + MoveBase.getBaseForNumber(move).getName());
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    Move m = new Move(getPower(), p);

    if (p.type1() != m.type() && p.type2() != m.type()) {
      add(1); // restore the removed machine
      return false;
    }

    // TODO : Finish this. Make sure to add(1) if they cancel.
    return false;
  }

}
