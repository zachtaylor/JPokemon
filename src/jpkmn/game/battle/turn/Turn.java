package jpkmn.game.battle.turn;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.battle.slot.Slot;

public abstract class Turn implements Comparable<Turn> {
  public Turn(Slot user) {
    _user = user;
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
   * Forces this turn to do a swap on execute. Overrides subclass
   * implementation.
   */
  public void changeToSwap() {
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
    return (String[]) _messages.toArray();
  }

  /**
   * Hook method provided for subclass implementation.
   */
  protected abstract void doExecute();

  private void doForcedSwap() {
    // TODO
  }

  private Slot _user;
  protected boolean _needSwap;
  private List<String> _messages;
}