package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import jpkmn.game.pokemon.Condition.Issue;

public class Round {
  public Round(Battle b) {
    _battle = b;
    _turns = new PriorityQueue<Turn>(b.getSlots().size(),
        new Turn.TurnComparator());
    _haveSelectedTurn = new ArrayList<Slot>();
    _forceNextAttack = new ArrayList<Slot>();
  }

  public int size() {
    return _turns.size();
  }

  public void add(Turn t) {
    if (_haveSelectedTurn.contains(t.getUserSlot())) return;
    _haveSelectedTurn.add(t.getUserSlot());
    _turns.add(t);
  }

  public void play() {
    for (Turn turn : _turns) {
      // TODO Execute each

      
      _battle.notifyAll(turn.getNotifications());
      
      if (turn.getUserSlot().getLeader().condition.contains(Issue.WAIT))
        _forceNextAttack.add(turn.getUserSlot());
      
      verifyTurnList();
    }

    setForcedNextAttacks();
  }

  private void verifyTurnList() {
    Slot slot, target;

    for (Turn turn : _turns) {
      slot = turn.getUserSlot();
      target = slot.getTarget(); // TODO measure this and do something

      if (!slot.getLeader().condition.getAwake()) {
        if (slot.getParty().countAwake() > 0)
          turn.changeToSwap();
        else {
          _turns.remove(turn);
          _battle.remove(turn.getUserSlot());
        }
      }
    }
  }

  private void setForcedNextAttacks() {
    for (Slot slot : _forceNextAttack)
      _battle.fight(slot);
  }

  private Battle _battle;
  private Queue<Turn> _turns;
  private List<Slot> _haveSelectedTurn, _forceNextAttack;
}
