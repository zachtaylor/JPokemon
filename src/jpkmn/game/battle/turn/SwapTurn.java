package jpkmn.game.battle.turn;

import jpkmn.game.battle.slot.Slot;

public class SwapTurn extends Turn {
  public SwapTurn(Slot user, int slotIndex) {
    super(user);
    _swapIndex = slotIndex;
  }

  public void doExecute() {
    slot().party().swap(0, _swapIndex);

    String trainerName = slot().trainer().name();
    String leaderName = slot().leader().name();
    addMessage(trainerName + " sent out " + leaderName);
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
  public void changeToSwap() {
    // No need for _needSwap. Skipping call to super.changeToSwap
  }

  private int _swapIndex;
}