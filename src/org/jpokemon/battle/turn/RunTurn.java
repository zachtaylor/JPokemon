package org.jpokemon.battle.turn;

import org.jpokemon.activity.BattleActivity;
import org.jpokemon.activity.PlayerManager;
import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.server.Message;
import org.jpokemon.server.MessageLevel;
import org.jpokemon.trainer.Player;

public class RunTurn extends Turn {
  public RunTurn(Slot user, Slot target) {
    super(user, target);
  }

  @Override
  protected void doExecute() {
    Battle battle = ((BattleActivity) PlayerManager.getActivity((Player) slot().trainer())).getBattle();
    double chance = 100;

    for (Slot s : battle) {
      if (slot().leader().level() < s.leader().level())
        chance -= 10 * (s.leader().level() - slot().leader().level());
      else if (slot().leader().level() > s.leader().level())
        chance += 7 * (slot().leader().level() - s.leader().level());
    }

    if ((chance / 250.0) > Math.random()) {
      Message message = new Message("SPEECH", "Got away safely!", MessageLevel.MESSAGE);
      PlayerManager.pushMessage((Player) slot().trainer(), message);
      battle.remove(slot());
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