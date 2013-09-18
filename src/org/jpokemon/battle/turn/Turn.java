package org.jpokemon.battle.turn;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;

public abstract class Turn implements Comparable<Turn> {
  private Battle battle;
  private Slot slot, targetSlot;

  public Turn(Battle b, Slot user, Slot target) {
    battle = b;
    slot = user;
    targetSlot = target;
  }

  /** Getter for the Battle this turn will be applied to **/
  public Battle battle() {
    return battle;
  }

  /**
   * Getter for the slot associated with this turn
   * 
   * @return Slot which is using this turn
   */
  public Slot slot() {
    return slot;
  }

  /**
   * Getter for the target Slot of this turn
   * 
   * @return Slot targeted by this Turn
   */
  public Slot target() {
    if (targetSlot == null) { return slot; }
    return targetSlot;
  }

  /**
   * Executes this turn. If {@link changeToSwap} was called, this turn will produce a swap between the leader and the first available awake pokemon. Otherwise,
   * subclass implementation will be called.
   */
  public abstract void execute();

  /**
   * Hook method provided for subclass implementation.
   * 
   * @return True if this Turn should be added to the queue for next Round after it has been executed
   */
  public abstract boolean reAdd();
}