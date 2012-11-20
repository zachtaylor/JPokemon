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
    _forceNextAttack = new ArrayList<Slot>();
  }

  public int size() {
    return _turns.size();
  }

  public void add(AbstractTurn t) {
    _turns.add(t);
  }

  public void play() {
    AbstractTurn turn;

    // Setup rivals
    for (Slot a : _battle) {
      for (Slot b : _battle) {
        if (a.id() != b.id())
          a.addRival(b.leader());
      }
    }

    while (!_turns.isEmpty()) {
      turn = _turns.remove();

      String[] message = turn.execute();

      for (Slot slot : _battle)
        slot.trainer().notify(message);

      if (turn.getUserSlot().leader().hasIssue(Issue.WAIT))
        _forceNextAttack.add(turn.getUserSlot());

      verifyTurnList();
    }

    executeConditionEffects();
    setForcedNextAttacks();
  }

  private void verifyTurnList() {
    Slot slot;

    for (AbstractTurn turn : _turns) {
      slot = turn.getUserSlot();

      if (slot.party().size() > 0 && !slot.leader().condition.awake()) {
        _battle.remove(slot.id());

        for (Slot s : _battle)
          s.removeRival(slot.leader());

        if (slot.party().awake() > 0) {
          turn.changeToSwap();
        }
      }

      if (slot.party().awake() == 0) {
        _turns.remove(turn);
        _battle.remove(slot.id());
      }
    }

    for (AbstractTurn turn : _turns) {
      boolean attackTargetMissing = turn instanceof AttackTurn
          && _battle.get(turn.getUserSlot().target().id()) == null;

      if (attackTargetMissing) {
        // TODO shit bricks
      }
    }
  }

  private void setForcedNextAttacks() {
    for (Slot slot : _forceNextAttack)
      _battle.add(slot.attack());

    _forceNextAttack = new ArrayList<Slot>();
  }

  private void executeConditionEffects() {
    for (Slot slot : _battle) {
      String[] messages = slot.leader().condition.applyEffects();
      for (Slot s : _battle)
        s.trainer().notify(messages);
    }
  }

  private Battle _battle;
  private Queue<AbstractTurn> _turns;
  private List<Slot> _forceNextAttack;
}