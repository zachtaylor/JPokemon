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

    while (!_turns.isEmpty()) {
      turn = _turns.remove();

      _battle.notifyAll(turn.getNotifications());

      if (turn.getUserSlot().leader().condition.contains(Issue.WAIT))
        _forceNextAttack.add(turn.getUserSlot());

      verifyTurnList();
    }

    setForcedNextAttacks();
  }

  private void verifyTurnList() {
    Slot slot;

    for (Turn turn : _turns) {
      slot = turn.getUserSlot();

      if (!slot.leader().condition.awake()) {
        if (slot.getParty().countAwake() > 0)
          turn.changeToSwap();
        else {
          _turns.remove(turn);
          _battle.removeLoser(slot.id());
        }
      }
    }

    for (Turn turn : _turns) {
      slot = turn.getUserSlot().getTarget();

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
