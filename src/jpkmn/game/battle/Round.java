package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;

import jpkmn.game.battle.turn.AbstractTurn;
import jpkmn.game.pokemon.Condition;

public class Round {
  public Round(Battle b) {
    _battle = b;
    _turns = new PriorityQueue<AbstractTurn>();
    new ArrayList<Slot>();
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
        if (!a.trainer().equals(b))
          a.addRival(b);
      }
    }

    while (!_turns.isEmpty()) {
      turn = _turns.remove();
      notifyAllTrainers(turn.execute());
      verifyTurnList();
    }

    applyConditionEffects();
    applyForceNextAttacks();
  }

  private void verifyTurnList() {
    Slot slot;

    for (AbstractTurn turn : _turns) {
      slot = turn.getUserSlot();

      if (slot.party().size() == 0 || slot.party().awake() == 0) {
        _turns.remove(turn);
        _battle.remove(slot);
      }
      else if (!slot.leader().condition.awake()) {
        for (Slot s : _battle)
          s.removeRival(slot);

        turn.changeToSwap();
      }

      if (!_battle.contains(slot.target().trainer())) {
        // TODO shit bricks
      }
    }
  }

  private void applyForceNextAttacks() {
    for (Slot slot : _battle)
      if (slot.leader().hasIssue(Condition.Issue.WAIT))
        _battle.add(slot.attack());
  }

  private void applyConditionEffects() {
    for (Slot slot : _battle) {
      String[] messages = slot.leader().condition.applyEffects();
      notifyAllTrainers(messages);
    }
  }

  private void notifyAllTrainers(String[] message) {
    for (Slot s : _battle)
      s.trainer().notify(message);
  }

  private Battle _battle;
  private Queue<AbstractTurn> _turns;
}