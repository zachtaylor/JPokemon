package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import jpkmn.game.battle.turn.AbstractTurn;
import jpkmn.game.battle.turn.AttackTurn;
import jpkmn.game.pokemon.Condition.Issue;

public class Round {
  public Round(Battle b) {
    _battle = b;
    _turns = new PriorityQueue<AbstractTurn>();
    _haveSelectedTurn = new ArrayList<Slot>();
    _forceNextAttack = new ArrayList<Slot>();
  }

  public int size() {
    return _turns.size();
  }

  public void add(AbstractTurn t) {
    if (_haveSelectedTurn.contains(t.getUserSlot()))
      return;
    _haveSelectedTurn.add(t.getUserSlot());
    _turns.add(t);
  }

  public void play() {
    AbstractTurn turn;

    // Setup rivals
    for (Slot a : _battle) {
      for (Slot b : _battle) {
        if (a.id() != b.id())
          a.rival(b.leader());
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

    for (AbstractTurn turn : _turns) {
      slot = turn.getUserSlot();

      if (slot.party().size() > 0 && !slot.leader().condition.awake()) {
        _battle.rewardFrom(slot.id());
        if (slot.party().awake() > 0)
          turn.changeToSwap();
      }

      if (slot.party().awake() == 0) {
        _turns.remove(turn);
        _battle.remove(slot.id());
      }
    }

    for (AbstractTurn turn : _turns) {
      boolean attackTargetMissing = turn instanceof AttackTurn
          && _battle.get(turn.getUserSlot().target()) == null;

      if (attackTargetMissing) {
        // TODO shit bricks
      }
    }
  }

  private void setForcedNextAttacks() {
    for (Slot slot : _forceNextAttack)
      _battle.add(slot.attack());
  }

  private Battle _battle;
  private Queue<AbstractTurn> _turns;
  private List<Slot> _haveSelectedTurn, _forceNextAttack;
}