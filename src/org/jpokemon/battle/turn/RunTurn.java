package org.jpokemon.battle.turn;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.trainer.Player;

public class RunTurn extends Turn {
  public RunTurn(Battle b, Slot user, Slot target) {
    super(b, user, target);
  }

  @Override
  public void execute() {
    Battle battle = (Battle) PlayerManager.getActivity((Player) slot().trainer());
    double chance = 100;

    for (Slot s : battle) {
      if (slot().leader().level() < s.leader().level())
        chance -= 10 * (s.leader().level() - slot().leader().level());
      else if (slot().leader().level() > s.leader().level()) chance += 7 * (slot().leader().level() - s.leader().level());
    }

    if ((chance / 250.0) > Math.random()) {
      // Message message = new Message("SPEECH", "Got away safely!", Message.Level.MESSAGE);
      // PlayerManager.pushMessage((Player) slot().trainer(), message);
      battle.remove(slot().trainer());
    }
    else
      battle().log("Didn't get away!");
  }

  @Override
  public boolean reAdd() {
    return false;
  }

  @Override
  public int compareTo(Turn turn) {
    if (turn instanceof RunTurn) return 0;

    return -1;
  }
}