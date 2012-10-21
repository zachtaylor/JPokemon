package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import jpkmn.Constants;
import jpkmn.game.pokemon.Condition.Issue;

public class Round {
  public Round(Battle b) {
    _battle = b;
    _turns = new PriorityQueue<Turn>(Constants.MAXBATTLESIZE,
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
    Turn turn;

    // Setup rivals
    for (Slot a : _battle) {
      for (Slot b : _battle) {
        if (a.id() != b.id()) a.rival(b.leader());
      }
    }

    while (!_turns.isEmpty()) {
      turn = _turns.remove();

      turn.execute();

      _battle.notifyAll(turn.getNotifications());

      if (turn.getUserSlot().leader().hasIssue(Issue.WAIT))
        _forceNextAttack.add(turn.getUserSlot());

      verifyTurnList();
    }

    setForcedNextAttacks();

  }

  private void verifyTurnList() {
    Slot slot;

    for (Turn turn : _turns) {
      slot = turn.getUserSlot();

      if (slot.party().size() > 0 && !slot.leader().condition.awake()) {
        _battle.rewardFrom(slot.id());
        if (slot.party().countAwake() > 0) turn.changeToSwap();
      }

      if (slot.party().countAwake() == 0) {
        _turns.remove(turn);
        _battle.remove(slot.id());
      }
    }

    for (Turn turn : _turns) {
      slot = turn.getUserSlot().target();

      if (_battle.get(slot.id()) == null) {
        // TODO shit bricks
      }
    }
  }

  private void setForcedNextAttacks() {
    for (Slot slot : _forceNextAttack)
      _battle.fight(slot.id());
  }

  private Battle _battle;
  private Queue<Turn> _turns;
  private List<Slot> _haveSelectedTurn, _forceNextAttack;
}