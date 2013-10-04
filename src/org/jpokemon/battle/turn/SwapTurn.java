package org.jpokemon.battle.turn;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;

public class SwapTurn extends Turn {
  private int _swapIndex;

  public SwapTurn(Battle b, Slot user, int swapIndex) {
    super(b, user, user);
    _swapIndex = swapIndex;
  }

  public static SwapTurn autoSwapTurn(Battle b, Slot user) {
    if (user.party().awake() == 0) { return null; }

    int autoSwapIndex = 0;
    while (autoSwapIndex < user.party().size() && !user.party().get(autoSwapIndex).awake()) {
      autoSwapIndex++;
    }

    return new SwapTurn(b, user, autoSwapIndex);
  }

  @Override
  public void execute() {
    slot().party().swap(0, _swapIndex);

    String trainerName = slot().trainer().id();
    String leaderName = slot().leader().name();
    battle().log(trainerName + " sent out " + leaderName);
  }

  @Override
  public boolean reAdd() {
    return false;
  }

  @Override
  public int compareTo(Turn turn) {
    if (turn instanceof RunTurn) return 1;
    if (turn instanceof SwapTurn) return 0;

    return -1;
  }
}