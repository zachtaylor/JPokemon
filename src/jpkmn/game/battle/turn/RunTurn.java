package jpkmn.game.battle.turn;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.battle.slot.Slot;

public class RunTurn extends Turn {
  public RunTurn(Slot user, Slot target) {
    super(user, target);
  }

  @Override
  protected void doExecute() {
    Battle battle = BattleRegistry.get(slot().trainer());
    double chance = 100;

    for (Slot s : battle) {
      if (slot().leader().level() < s.leader().level())
        chance -= 10 * (s.leader().level() - slot().leader().level());
      else if (slot().leader().level() > s.leader().level())
        chance += 7 * (slot().leader().level() - s.leader().level());
    }

    if ((chance / 250.0) > Math.random()) {
      battle.remove(slot());
      addMessage("Got away successfully!");
    }
    else
      addMessage("Didn't get away!");
  }

  @Override
  public boolean reAdd() {
    return false;
  }

  @Override
  public int compareTo(Turn turn) {
    if (turn instanceof RunTurn)
      return 0;

    return -1;
  }
}