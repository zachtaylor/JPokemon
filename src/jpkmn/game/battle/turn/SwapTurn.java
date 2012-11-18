package jpkmn.game.battle.turn;

import jpkmn.game.battle.Slot;

public class SwapTurn extends AbstractTurn {
  public SwapTurn(Slot user, int slotIndex) {
    super(user);
    _swapIndex = slotIndex;
  }

  public void execute() {
    _user.party().swap(0, _swapIndex);

    String trainerName = _user.trainer().name();
    String leaderName = _user.leader().name();
    _messages.add(trainerName + " sent out " + leaderName);
  }

  @Override
  public int compareTo(AbstractTurn turn) {
    if (turn.needSwap() || turn instanceof SwapTurn)
      return 0;
    if (turn instanceof RunTurn)
      return 1;
    return -1;
  }

  @Override
  public void changeToSwap() {
    // No need for _needSwap
  }

  private int _swapIndex;
}