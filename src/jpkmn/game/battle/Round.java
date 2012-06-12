package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Round {
  public Round(Battle b) {
    _battle = b;
    _turns = new PriorityBlockingQueue<Turn>(2, new Turn.TurnComparator());
    _haveSelectedTurn = new ArrayList<Slot>();
    _forceNextAttack = new ArrayList<Slot>();
  }

  public void add(Turn t) {
    if (_haveSelectedTurn.contains(t.getUserSlot())) return;
    _haveSelectedTurn.add(t.getUserSlot());
    _turns.add(t);

    run();
  }

  private void run() {
    if (_turns.size() != _battle.getSlots().size()) return;

    // TODO the hard stuff - execute each
  }

  private Battle _battle;
  private Queue<Turn> _turns;
  private List<Slot> _haveSelectedTurn, _forceNextAttack;
}
