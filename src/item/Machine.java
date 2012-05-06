package item;

import lib.MoveBase;

import jpkmn.Driver;
import pokemon.Pokemon;
import pokemon.move.Move;

public class Machine extends Item {
  private int a;
  
  public Machine(int move, int quantity) {
    super(move, quantity, "TM-" + MoveBase.getBaseForNumber(move).getName());
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) {
      Driver.log(Machine.class, "Not enough to use type " + getName());
      return false;
    }
    
    Move m = new Move(getPower(), p);
    
    if (p.type1() != m.type() && p.type2() != m.type()) {
      Driver.log(Machine.class, p.name() + " cannot learn move "+m.name());
      add(1); // restore the removed machine
      return false;
    }
    Driver.log(Machine.class, p.name() + " can learn move "+m.name());
    
    // TODO : Finish this. Make sure to add(1) if they cancel.
    gui.Tools.askMove(p, m);
    return false;
  }

}
