package org.jpokemon.battle.turn;

import java.util.PriorityQueue;
import java.util.Queue;

import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;

public class Round {
  public Round(Battle b) {
    _battle = b;
    _turns = new PriorityQueue<Turn>();
  }

  public int size() {
    return _turns.size();
  }

  public void add(Turn t) {
    _turns.add(t);
  }

  public void execute() {
    // Setup rivals
    for (Slot a : _battle) {
      for (Slot b : _battle) {
        if (!a.trainer().equals(b))
          a.addRival(b);
      }
    }

    // MADNESS
    while (!_turns.isEmpty()) {
      Turn turn = _turns.remove();
      turn.execute();
      notifyAllTrainers(turn.getMessages());
      verifyTurnList();

      if (turn.reAdd())
        _battle.addTurn(turn);
    }

    applyEndOfRoundEffects();
  }

  private void verifyTurnList() {
    Slot slot;

    for (Turn turn : _turns) {
      slot = turn.slot();

      if (slot.party().awake() == 0) {
        _turns.remove(turn);
        _battle.remove(slot);
      }
      else if (!slot.leader().awake()) {
        for (Slot s : _battle)
          s.removeRival(slot);

        turn.forceSwap();
      }

      if (!_battle.contains(turn.target().trainer())) {
        // TODO shit bricks
      }
    }
  }

  private void applyEndOfRoundEffects() {
    for (Slot slot : _battle) {
      // Condition effects
      slot.leader().applyConditionEffects();
      notifyAllTrainers(slot.leader().lastConditionMessage());

      // Slot effects
      String[] messages = slot.applySlotEffects();
      notifyAllTrainers(messages);
    }
  }

  private void notifyAllTrainers(String[] message) {
    for (Slot s : _battle)
      s.trainer().notify(message);
  }

  private Battle _battle;
  private Queue<Turn> _turns;
}