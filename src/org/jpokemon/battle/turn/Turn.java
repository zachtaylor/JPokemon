package org.jpokemon.battle.turn;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.Pokemon;

public abstract class Turn implements Comparable<Turn> {
  public Turn(Slot user, Slot target) {
    _user = user;
    _target = target;
    _needSwap = false;
    _messages = new ArrayList<String>();
  }

  /**
   * Getter for the slot associated with this turn
   * 
   * @return Slot which is using this turn
   */
  public Slot slot() {
    return _user;
  }

  /**
   * Getter for the target Slot of this turn
   * 
   * @return Slot targeted by this Turn
   */
  public Slot target() {
    return _target;
  }

  /**
   * Executes this turn. If {@link changeToSwap} was called, this turn will
   * produce a swap between the leader and the first available awake pokemon.
   * Otherwise, subclass implementation will be called.
   */
  public void execute() {
    if (_needSwap) {
      doForcedSwap();
      return;
    }

    doExecute();
  }

  /**
   * Forces this turn to do a swap on execute, instead of it's original
   * intention.
   */
  public void forceSwap() {
    _needSwap = true;
  }

  /**
   * Adds a message to the summary of what happened in this turn
   * 
   * @param message Message to add
   */
  public void addMessage(String message) {
    _messages.add(message);
  }

  /**
   * Reports all relavent messages produced by executing this turn
   * 
   * @return Messages to alert players of
   */
  public String[] getMessages() {
    return _messages.toArray(new String[_messages.size()]);
  }

  /**
   * Hook method provided for subclass implementation.
   */
  protected abstract void doExecute();

  /**
   * Hook method provided for subclass implementation.
   * 
   * @return True if this Turn should be added to the queue for next Round after
   *         it has been executed
   */
  public abstract boolean reAdd();

  private void doForcedSwap() {
    int swapIndex = 0;

    for (Pokemon p : _user.party()) {
      if (p.awake())
        break;
      else
        swapIndex++;
    }

    _user.party().swap(0, swapIndex);

    String trainerName = slot().trainer().name();
    String leaderName = slot().leader().name();
    addMessage(trainerName + " sent out " + leaderName);
  }

  private Slot _user, _target;
  protected boolean _needSwap;
  private List<String> _messages;
}