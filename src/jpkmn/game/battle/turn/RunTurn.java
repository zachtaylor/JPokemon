package jpkmn.game.battle.turn;

import jpkmn.game.battle.Battle;
import jpkmn.game.battle.Slot;

public class RunTurn extends AbstractTurn {
  public RunTurn(Slot user) {
    super(user);
  }

  public void execute() {
    if (needSwap()) {
      executeForcedSwap();
      return;
    }

    double chance = 100;
    Battle battle = _user.battle();

    for (Slot s : battle) {
      if (_user.leader().level() < s.leader().level())
        chance -= 10 * (s.leader().level() - _user.leader().level());
      else
        chance += 7 * (_user.leader().level() - s.leader().level());
    }

    if ((chance / 250.0) > Math.random()) {
      battle.remove(_user.id());
      _messages.add("Got away successfully!");
    }
    else
      _messages.add("Didn't get away!");
  }

  @Override
  public int compareTo(AbstractTurn turn) {
    if (turn instanceof RunTurn)
      return 0;
    return -1;
  }
}