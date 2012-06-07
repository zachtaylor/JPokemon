package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;

import jpkmn.exceptions.BattleEndException;
import jpkmn.game.pokemon.*;
import jpkmn.game.pokemon.move.*;

public class Battle {
  boolean wild = true, gym = false;

  public Battle() {
    _turns = new ArrayList<Turn>();
  }

  /**
   * Chooses moves and attacks.
   * 
   * @throws BattleEndException to end the battle
   */
  public void fight(Slot slot)  {
    if (!slot.chooseMove()) return;
    if (!slot.chooseAttackTarget()) return;

    _turns.add(slot.attack());
  }

  public void item(Slot slot) {
    if (!slot.chooseItem()) return;
    if (!slot.chooseItemTarget()) return;

    _turns.add(slot.item());
  }

  /**
   * Asks the user about swapping Pokemon. If they swap, then the enemy attacks
   * 
   * @throws BattleEndException If the battle ends
   */
  public void swap(Slot slot) {
    if (!slot.chooseSwapPosition()) return;
    
    _turns.add(slot.swap());
  }

  /**
   * Calculates if the user escapes on a run attempt. If not, then the enemy
   * attacks.
   * 
   * @throws BattleEndException If the battle ends
   */
  public void run(Slot slot) {
    if (!slot.run()) return;
    
    // TODO Remove the slot from the battle
  }

  /**
   * Computes damage through formula
   * 
   * @param move Move used
   * @param user User of the move
   * @param victim Victim of the move
   * @return Integer value of the move
   */
  public static int computeDamage(Move move, Pokemon victim) {
    Pokemon user = move.pkmn;

    double damage = 1.0, L = user.level(), A = 1.0, P = move.power(), D = 0, STAB = move
        .STAB(), E = move.effectiveness(victim), R = Math.random() * .15 + .85;

    if (move.style() == MoveStyle.SPECIAL) {
      A = user.stats.stk.cur();
      D = victim.stats.sdf.cur();
    }
    else if (move.style() == MoveStyle.PHYSICAL) {
      A = user.stats.atk.cur();
      D = victim.stats.def.cur();
    }
    else if (move.style() == MoveStyle.OHKO) {
      A = 10000000;
      D = 1;
    }
    else if (move.style() == MoveStyle.DELAY) {
      if (user.stats.atk.cur() > user.stats.stk.cur())
        A = user.stats.atk.cur();
      else
        A = user.stats.stk.cur();
      if (victim.stats.def.cur() > victim.stats.sdf.cur())
        D = victim.stats.def.cur();
      else
        D = victim.stats.sdf.cur();
    }

    damage = (((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R;

    if (damage < 1 && E != 0) damage = 1;

    return (int) damage;
  }

  /**
   * Calculates the confused damage a Pokemon does to itself. This method
   * exists here to unify all damage calculations.
   * 
   * @param p Pokemon to calculate for
   * @return Damage done
   */
  public static int confusedDamage(Pokemon p) {
    // For more info, see computeDamage
    double L = p.level(), A = p.stats.atk.cur(), P = 40, D = p.stats.def.cur(), STAB = 1, E = 1, R = 1.00;
    return (int) ((((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R);
  }

  private List<Turn> _turns;
}