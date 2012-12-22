package jpkmn.game.battle.turn;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.battle.slot.Slot;

public class RunTurn extends AbstractTurn {
  public RunTurn(Slot user) {
    super(user);
  }

  public String[] execute() {
    if (needSwap())
      return executeForcedSwap();

    Battle battle = BattleRegistry.get(_user.trainer());
    double chance = 100;

    for (Slot s : battle) {
      if (_user.leader().level() < s.leader().level())
        chance -= 10 * (s.leader().level() - _user.leader().level());
      else if (_user.leader().level() > s.leader().level())
        chance += 7 * (_user.leader().level() - s.leader().level());
    }

    if ((chance / 250.0) > Math.random()) {
      battle.remove(_user);
      _messages.add("Got away successfully!");
    }
    else
      _messages.add("Didn't get away!");

    return getNotifications();
  }

  @Override
  public int compareTo(AbstractTurn turn) {
    if (turn instanceof RunTurn)
      return 0;
    return -1;
  }
}