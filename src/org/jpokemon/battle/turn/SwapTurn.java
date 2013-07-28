package org.jpokemon.battle.turn;

import org.jpokemon.battle.slot.Slot;

public class SwapTurn extends Turn {
  public SwapTurn(Slot user, Slot target, int swapIndex) {
    super(user, target);
    _swapIndex = swapIndex;
  }

  @Override
  public void doExecute() {
    slot().party().swap(0, _swapIndex);

    String trainerName = slot().trainer().getName();
    String leaderName = slot().leader().name();
    addMessage(trainerName + " sent out " + leaderName);
  }

  @Override
  public boolean reAdd() {
    return false;
  }

  @Override
  public int compareTo(Turn turn) {
    if (turn instanceof RunTurn)
      return 1;
    if (turn._needSwap || turn instanceof SwapTurn)
      return 0;

    return -1;
  }

  @Override
  public void forceSwap() {
    // No need for _needSwap. Skipping call to super.changeToSwap
  }

  private int _swapIndex;
}